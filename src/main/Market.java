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
            if (order.getDirection() == DIRECTION.BUY) {
                
            } else if (order.getDirection() == DIRECTION.SELL) {

            } else {
                throw new UnsupportedOperationException("Order direction not specified");
            }
        } else if(order instanceof MarketOrder) {
            if (order.getDirection() == DIRECTION.BUY) {

            } else if (order.getDirection() == DIRECTION.SELL) {

            } else {
                throw new UnsupportedOperationException("Order direction not specified");
            }
        } else {
            throw new UnsupportedOperationException("Order type not specified");
        }

    }

    public List<Trade> getAllResultingTrades() {
        return trades;
    }
}
