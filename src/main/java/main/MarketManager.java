package main;

import java.util.List;
import dataObjects.Order;
import dataObjects.Trade;

public class MarketManager {

    public static List<Trade> getResultingTrades(List<Order> orders) {
        Market market = new Market();
        orders.forEach(market::completeTimestep);
        return market.getAllResultingTrades();
    }
}
