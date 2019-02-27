package main;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import main.DataObjects.Order;
import main.DataObjects.Trade;

class Market {

    final private static Logger LOGGER = Logger.getLogger("MARKET_LOGGER");

    private List<Trade> trades;



    public Market() {
        LOGGER.finer("Creating Market");
        trades = new ArrayList<>();
    }

    public void processOrder(Order order) {
        LOGGER.finer(String.format("Processing order %s", order.toString()));

    }

    public List<Trade> getAllResultingTrades() {
        return trades;
    }
}
