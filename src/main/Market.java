package main;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;
import main.DataStructures.LimitOrderQueue;
import main.DataStructures.LimitOrderQueue.SORTING_METHOD;
import main.DataObjects.LimitOrder;
import main.DataObjects.MarketOrder;
import main.DataObjects.Order;
import main.DataObjects.Order.DIRECTION;
import main.DataObjects.Trade;

class Market {

    final private static Logger LOGGER = Logger.getLogger("MARKET_LOGGER");

    private final List<Trade> trades = new ArrayList<>();
    private final SortedSet<LimitOrder> sellLimitOrders = new LimitOrderQueue(SORTING_METHOD.PRICE_ASC);
    private final SortedSet<LimitOrder> buyLimitOrders = new LimitOrderQueue(SORTING_METHOD.PRICE_DECS);
    private final SortedSet<MarketOrder> buyMarketOrders = new TreeSet<>((a, b) -> a.getOrderId() - b.getOrderId());
    private final SortedSet<MarketOrder> sellMarketOrders = new TreeSet<>((a, b) -> a.getOrderId() - b.getOrderId());

    public Market() {
        LOGGER.finer("Creating Market");
    }

    public void processOrder(Order order) {
        LOGGER.finer(String.format("Processing order %s", order.toString()));
        if(order instanceof LimitOrder) {
            processLimitOrder((LimitOrder) order);
        } else if(order instanceof MarketOrder) {
            processMarketOrder((MarketOrder) order);
        } else {
            throw new UnsupportedOperationException("Order type not specified");
        }
        LOGGER.finer("Sell Limit Orders: " + sellLimitOrders.toString());
        LOGGER.finer("Buy Limit Orders: " + buyLimitOrders.toString());
        LOGGER.finer("Sell Market Orders: " + buyMarketOrders.toString());
        LOGGER.finer("Sell Market Orders: " + sellMarketOrders.toString());
        LOGGER.finer("Trades: " + trades.toString());
    }

    private void processMarketOrder(MarketOrder marketOrder) {
        if (marketOrder.getDirection() == DIRECTION.BUY) {
            processDirectedMarketOrder(marketOrder, sellLimitOrders, buyMarketOrders);
        } else if (marketOrder.getDirection() == DIRECTION.SELL) {
            processDirectedMarketOrder(marketOrder, buyLimitOrders, sellMarketOrders);
        } else {
            throw new UnsupportedOperationException("Order direction not supported");
        }
    }

    private void processDirectedMarketOrder(MarketOrder marketOrder,
        SortedSet<LimitOrder> limitOrders, SortedSet<MarketOrder> marketOrders) {
        LOGGER.finest("Checking Limit Order queue");
        if(limitOrders.isEmpty()) {
            LOGGER.finest("Limit Order queue empty, so adding to market order queue");
            marketOrders.add(marketOrder);
        } else {
            LimitOrder limitOrder = limitOrders.first();
            LOGGER.finest("Limit Order queue not empty, so trading with best limit order: " + limitOrder.toString());
            limitOrders.remove(limitOrder);
            makeTrade(marketOrder, limitOrder, limitOrder.getLimit());
        }
    }

    private void makeTrade(Order a, Order b, float limit) {
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
        if (limitOrder.getDirection() == DIRECTION.BUY) {
            processDirectedLimitOrder(limitOrder, sellMarketOrders, buyLimitOrders, sellLimitOrders);
        } else if (limitOrder.getDirection() == DIRECTION.SELL) {
            processDirectedLimitOrder(limitOrder, buyMarketOrders, sellLimitOrders, buyLimitOrders);
        } else {
            throw new UnsupportedOperationException("Order direction not supported");
        }
    }

    private void processDirectedLimitOrder(LimitOrder limitOrder,
        SortedSet<MarketOrder> marketOrders,
        SortedSet<LimitOrder> sameTypeLimitOrders,
        SortedSet<LimitOrder> oppositeTypeLimitOrders) {
        LOGGER.finest("Checking Market Order queue");
        if(marketOrders.isEmpty()) {
            LOGGER.finest("Market Order queue empty, so checking Limit orders");
            if(oppositeTypeLimitOrders.isEmpty()) {
                LOGGER.finest("Limit Order queue empty, so adding to limit order queue");
                sameTypeLimitOrders.add(limitOrder);
            } else {
                LimitOrder otherLimitOrder = oppositeTypeLimitOrders.first();
                LOGGER.finest("Limit Order queue not empty, so checking if best order matches: " + otherLimitOrder.toString());

                if(limitsMatch(limitOrder, otherLimitOrder)) {
                    LOGGER.finest("Limits match so completing trade");
                    oppositeTypeLimitOrders.remove(otherLimitOrder);
                    makeTrade(limitOrder, otherLimitOrder, otherLimitOrder.getLimit());
                } else {
                    LOGGER.finest("Limits do not match so adding to limit order queue");
                    sameTypeLimitOrders.add(limitOrder);
                }
            }
        } else {
            LOGGER.finest("Market Order queue not empty, so trading with oldest order: " + limitOrder.toString());
            MarketOrder marketOrder = marketOrders.first();
            marketOrders.remove(marketOrder);
            makeTrade(marketOrder, limitOrder, limitOrder.getLimit());
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

    public List<Trade> getAllResultingTrades() {
        return trades;
    }
}
