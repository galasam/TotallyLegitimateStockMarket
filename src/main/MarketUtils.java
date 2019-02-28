package main;

import java.util.SortedSet;
import java.util.logging.Logger;
import main.DataObjects.ReadyOrder;
import main.DataObjects.ReadyOrder.DIRECTION;
import main.DataObjects.ReadyOrder.TIME_IN_FORCE;
import main.DataObjects.Trade;
import main.DataStructures.TickerData;

public class MarketUtils {

    final private static Logger LOGGER = Logger.getLogger("MARKET_LOGGER");

    public static Trade constructTrade(ReadyOrder a, ReadyOrder b, float limit, TickerData ticketData) {
        ticketData.setLastExecutedTradePrice(limit);
        if(a.getDirection().equals(DIRECTION.BUY)) {
            Trade trade = new Trade(
                a.getOrderId(),
                b.getOrderId(),
                a.getQuantity(),
                limit);
            LOGGER.finest("Making Buy trade: " + trade.toString());
            return trade;
        } else if(a.getDirection().equals(DIRECTION.SELL)) {
            Trade trade = new Trade(
                b.getOrderId(),
                a.getOrderId(),
                a.getQuantity(),
                limit);
            LOGGER.finest("Making Sell trade: " + trade.toString());
            return trade;
        } else {
            throw new UnsupportedOperationException("Order direction not supported");
        }
    }

    public static <E extends ReadyOrder> void queueIfTimeInForce(E order,
        SortedSet<E> sameDirectionOrders) {
        if(order.getTimeInForce().equals(TIME_IN_FORCE.GTC)) {
            LOGGER.finest("Time in force is GTC so add to queue");
            sameDirectionOrders.add(order);
        } else if (order.getTimeInForce().equals(TIME_IN_FORCE.FOK)) {
            LOGGER.finest("Time in force is FOK so drop");
        } else {
            throw new UnsupportedOperationException("TIME IN FORCE mode not supported");
        }
    }

}
