package main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.logging.Logger;
import main.DataObjects.ReadyOrder;
import main.DataObjects.ReadyOrder.TIME_IN_FORCE;
import main.DataObjects.LimitOrder;
import main.DataObjects.MarketOrder;
import main.DataObjects.Order;
import main.DataObjects.ReadyOrder.DIRECTION;
import main.DataObjects.StopOrder;
import main.DataObjects.Trade;
import main.DataStructures.TickerData;

class Market {

    final private static Logger LOGGER = Logger.getLogger("MARKET_LOGGER");

    private final List<Trade> trades = new ArrayList<>();
    private final Map<String, TickerData> tickerQueues = new TreeMap<>();
    private final List<StopOrder> stopOrders = new LinkedList<>();

    Market() {
        LOGGER.finer("Creating Market");
    }

    void completeTimestep(Order order) {
        LOGGER.finer("Processing Triggered Stop Orders");
        processOrder(order);
        processTriggeredStopOrders();
    }

    private void processOrder(Order order) {
        LOGGER.finer(String.format("Processing order %s", order.toString()));
        if(order instanceof StopOrder) {
            stopOrders.add((StopOrder) order);
        } else if(order instanceof LimitOrder) {
            processLimitOrder((LimitOrder) order);
        } else if(order instanceof MarketOrder) {
            processMarketOrder((MarketOrder) order);
        } else {
            throw new UnsupportedOperationException("Order type not specified");
        }
        LOGGER.finer("Ticker queues: " + tickerQueues.toString());
        LOGGER.finer("Stop Orders: " + stopOrders.toString());
        LOGGER.finer("Trades: " + trades.toString());
    }

    private void processTriggeredStopOrders() {
        Iterator<StopOrder> it = stopOrders.iterator();
        while(it.hasNext()) {
            StopOrder stopOrder = it.next();
            LOGGER.finer("Testing Trigger on: " + stopOrder.toString());
            if(isStopLossTriggered(stopOrder)) {
                LOGGER.finer("Stop Order Triggered");
                it.remove();
                ReadyOrder readyOrder = stopOrder.getReadyOrder();
                processOrder(readyOrder);
            } else {
                LOGGER.finer("Stop Order not Triggered");
            }
        }
    }

    private boolean isStopLossTriggered(StopOrder stopOrder) {
        ReadyOrder readyOrder = stopOrder.getReadyOrder();
        if(readyOrder.getDirection().equals(DIRECTION.BUY)) {
            return stopOrder.getTriggerPrice() <= getTickerQueueGroup(readyOrder).getLastExecutedTradePrice();
        } else if(readyOrder.getDirection().equals(DIRECTION.SELL)) {
            return stopOrder.getTriggerPrice() >= getTickerQueueGroup(readyOrder).getLastExecutedTradePrice();
        } else {
            throw new UnsupportedOperationException("Order direction not supported");
        }
    }

    private void processMarketOrder(MarketOrder marketOrder) {
        TickerData tickerData = getTickerQueueGroup(marketOrder);
        if (marketOrder.getDirection() == DIRECTION.BUY) {
            processDirectedMarketOrder(marketOrder, tickerData,
                tickerData.getSellLimitOrders(),tickerData.getBuyMarketOrders());
        } else if (marketOrder.getDirection() == DIRECTION.SELL) {
            processDirectedMarketOrder(marketOrder, tickerData,
                tickerData.getBuyLimitOrders(), tickerData.getSellMarketOrders());
        } else {
            throw new UnsupportedOperationException("Order direction not supported");
        }
    }

    private TickerData getTickerQueueGroup(ReadyOrder marketOrder) {
        TickerData queues = tickerQueues.get(marketOrder.getTicker());
        if(queues == null) {
            queues = new TickerData();
            tickerQueues.put(marketOrder.getTicker(), queues);
        }
        return queues;
    }

    private void processDirectedMarketOrder(MarketOrder marketOrder, TickerData tickerData,
        SortedSet<LimitOrder> limitOrders, SortedSet<MarketOrder> marketOrders) {
        LOGGER.finest("Checking Limit Order queue");
        if(limitOrders.isEmpty()) {
            LOGGER.finest("Limit Order queue empty, so check if time in force");
            if(marketOrder.getTimeInForce().equals(TIME_IN_FORCE.GTC)) {
                LOGGER.finest("Time in force is GTC so add to queue");
                marketOrders.add(marketOrder);
            } else {
                LOGGER.finest("Time in force is FOK so drop");
            }
        } else {
            LimitOrder limitOrder = limitOrders.first();
            LOGGER.finest("Limit Order queue not empty, so trading with best limit order: " + limitOrder.toString());
            limitOrders.remove(limitOrder);
            makeTrade(marketOrder, limitOrder, limitOrder.getLimit(), tickerData);
        }
    }

    private void makeTrade(ReadyOrder a, ReadyOrder b, float limit, TickerData ticketData) {
        ticketData.setLastExecutedTradePrice(limit);
        if(a.getDirection().equals(DIRECTION.BUY)) {
            Trade trade = new Trade(
                a.getOrderId(),
                b.getOrderId(),
                a.getQuantity(),
                limit);
            LOGGER.finest("Making Buy trade: " + trade.toString());
            trades.add(trade);
        } else if(a.getDirection().equals(DIRECTION.SELL)) {
            Trade trade = new Trade(
                b.getOrderId(),
                a.getOrderId(),
                a.getQuantity(),
                limit);
            LOGGER.finest("Making Sell trade: " + trade.toString());
            trades.add(trade);
        } else {
            throw new UnsupportedOperationException("Order direction not supported");
        }
    }

    private void processLimitOrder(LimitOrder limitOrder) {
        TickerData tickerData = getTickerQueueGroup(limitOrder);
        if (limitOrder.getDirection() == DIRECTION.BUY) {
            processDirectedLimitOrder(limitOrder, tickerData,
                tickerData.getSellMarketOrders(),
                tickerData.getBuyLimitOrders(),
                tickerData.getSellLimitOrders());
        } else if (limitOrder.getDirection() == DIRECTION.SELL) {
            processDirectedLimitOrder(limitOrder, tickerData,
                tickerData.getBuyMarketOrders(),
                tickerData.getSellLimitOrders(),
                tickerData.getBuyLimitOrders());
        } else {
            throw new UnsupportedOperationException("Order direction not supported");
        }
    }

    private void processDirectedLimitOrder(LimitOrder limitOrder, TickerData tickerData,
        SortedSet<MarketOrder> marketOrders,
        SortedSet<LimitOrder> sameTypeLimitOrders,
        SortedSet<LimitOrder> oppositeTypeLimitOrders) {
        LOGGER.finest("Checking Market Order queue");
        if(marketOrders.isEmpty()) {
            LOGGER.finest("Market Order queue empty, so checking Limit orders");
            if(oppositeTypeLimitOrders.isEmpty()) {
                LOGGER.finest("Limit Order queue empty, so check if time in force");
                queueIfTimeInForce(limitOrder, sameTypeLimitOrders);
            } else {
                LimitOrder otherLimitOrder = oppositeTypeLimitOrders.first();
                LOGGER.finest("Limit Order queue not empty, so checking if best order matches: " + otherLimitOrder.toString());

                if(limitsMatch(limitOrder, otherLimitOrder)) {
                    LOGGER.finest("Limits match so completing trade");
                    oppositeTypeLimitOrders.remove(otherLimitOrder);
                    makeTrade(limitOrder, otherLimitOrder, otherLimitOrder.getLimit(), tickerData);
                } else {
                    LOGGER.finest("Limits do not match, so check if time in force");
                    queueIfTimeInForce(limitOrder, sameTypeLimitOrders);
                }
            }
        } else {
            LOGGER.finest("Market Order queue not empty, so trading with oldest order: " + limitOrder.toString());
            MarketOrder marketOrder = marketOrders.first();
            marketOrders.remove(marketOrder);
            makeTrade(marketOrder, limitOrder, limitOrder.getLimit(), tickerData);
        }
    }

    private void queueIfTimeInForce(LimitOrder limitOrder,
        SortedSet<LimitOrder> sameTypeLimitOrders) {
        if(limitOrder.getTimeInForce().equals(TIME_IN_FORCE.GTC)) {
            LOGGER.finest("Time in force is GTC so add to queue");
            sameTypeLimitOrders.add(limitOrder);
        } else if (limitOrder.getTimeInForce().equals(TIME_IN_FORCE.FOK)) {
            LOGGER.finest("Time in force is FOK so drop");
        } else {
            throw new UnsupportedOperationException("TIME IN FORCE mode not supported");
        }
    }

    private boolean limitsMatch(LimitOrder limitOrder, LimitOrder otherLimitOrder) {
        if(limitOrder.getDirection().equals(DIRECTION.BUY)) {
            return limitOrder.getLimit() >= otherLimitOrder.getLimit();
        } else if(limitOrder.getDirection().equals(DIRECTION.SELL)) {
            return limitOrder.getLimit() <= otherLimitOrder.getLimit();
        } else {
            throw new UnsupportedOperationException("Order direction not supported");
        }
    }

    List<Trade> getAllResultingTrades() {
        return trades;
    }
}
