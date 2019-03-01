package main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import main.DataObjects.Order;
import main.DataObjects.ReadyOrder;
import main.DataObjects.StopOrder;
import main.DataObjects.Trade;
import main.DataStructures.MarketState;

public class Market {

    final private static Logger LOGGER = Logger.getLogger("MARKET_LOGGER");

    private final List<Trade> trades = new ArrayList<>();
    private final MarketState marketState = new MarketState();

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

        order.process(marketState).ifPresent(trades::add);

        LOGGER.finer("Ticker queues: " + marketState.getTickerQueues().toString());
        LOGGER.finer("Stop Orders: " + marketState.getStopOrders().toString());
        LOGGER.finer("Trades: " + trades.toString());
    }

    private void processTriggeredStopOrders() {
        Iterator<StopOrder> it = marketState.getStopOrders().iterator();
        while(it.hasNext()) {
            StopOrder stopOrder = it.next();
            LOGGER.finer("Testing Trigger on: " + stopOrder.toString());
            if(stopOrder.isTriggered()) {
                LOGGER.finer("Stop Order Triggered");
                it.remove();
                ReadyOrder readyOrder = stopOrder.getReadyOrder();
                processOrder(readyOrder);
            } else {
                LOGGER.finer("Stop Order not Triggered");
            }
        }
    }

    List<Trade> getAllResultingTrades() {
        return trades;
    }
}
