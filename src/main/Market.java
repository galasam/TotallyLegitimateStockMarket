package main;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;
import main.DataObjects.DataStructures.LimitOrderQueue;
import main.DataObjects.DataStructures.LimitOrderQueue.SORTING_METHOD;
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
        if(limitOrders.isEmpty()) {
            marketOrders.add(marketOrder);
        } else {
            LimitOrder limitOrder = limitOrders.first();
            limitOrders.remove(limitOrder);
            makeTrade(marketOrder, limitOrder, limitOrder.getLimit());
        }
    }

    private void makeTrade(Order a, Order b, float limit) {
        if(a.getDirection().equals(DIRECTION.BUY)) {
            trades.add(new Trade(
                a.getOrderId(),
                b.getOrderId(),
                a.getQuantity(),
                limit));
        } else if(a.getDirection().equals(DIRECTION.SELL)) {
            trades.add(new Trade(
                b.getOrderId(),
                a.getOrderId(),
                a.getQuantity(),
                limit));
        } else {
            throw new UnsupportedOperationException("Order direction not supported");
        }
    }

    private void processLimitOrder(LimitOrder limitOrder) {
        if (limitOrder.getDirection() == DIRECTION.BUY) {
            processDirectedLimitOrder(limitOrder, sellMarketOrders, buyLimitOrders);
        } else if (limitOrder.getDirection() == DIRECTION.SELL) {
            processDirectedLimitOrder(limitOrder, buyMarketOrders, sellLimitOrders);
        } else {
            throw new UnsupportedOperationException("Order direction not supported");
        }
    }

    private void processDirectedLimitOrder(LimitOrder limitOrder,
        SortedSet<MarketOrder> marketOrders, SortedSet<LimitOrder> limitOrders) {
        if(marketOrders.isEmpty()) {
            if(limitOrders.isEmpty()) {
                limitOrders.add(limitOrder);
            } else {
                LimitOrder otherLimitOrder = limitOrders.first();

                if(limitsMatch(limitOrder, otherLimitOrder)) {
                    limitOrders.remove(otherLimitOrder);
                    makeTrade(limitOrder, otherLimitOrder, otherLimitOrder.getLimit());
                } else {
                    limitOrders.add(limitOrder);
                }
            }
        } else {
            MarketOrder marketOrder = marketOrders.first();
            marketOrders.remove(marketOrder);
            makeTrade(marketOrder, limitOrder, limitOrder.getLimit());
        }
    }

    private boolean limitsMatch(LimitOrder limitOrder, LimitOrder otherLimitOrder) {
        if(limitOrder.getDirection().equals(DIRECTION.BUY)) {
            return limitOrder.getLimit() <= otherLimitOrder.getLimit();
        } else if(limitOrder.getDirection().equals(DIRECTION.SELL)) {
            return limitOrder.getLimit() >= otherLimitOrder.getLimit();
        } else {
            throw new UnsupportedOperationException("Order direction not supported");
        }
    }

    public List<Trade> getAllResultingTrades() {
        return trades;
    }
}
