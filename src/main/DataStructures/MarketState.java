package main.DataStructures;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import main.DataObjects.ReadyOrder;
import main.DataObjects.StopOrder;

public class MarketState {

    private final Map<String, TickerData> tickerQueues = new TreeMap<>();
    private final List<StopOrder> stopOrders = new LinkedList<>();


    public Map<String, TickerData> getTickerQueues() {
        return tickerQueues;
    }

    public List<StopOrder> getStopOrders() {
        return stopOrders;
    }

    public TickerData getTickerQueueGroup(ReadyOrder marketOrder) {
        TickerData queues = tickerQueues.get(marketOrder.getTicker());
        if(queues == null) {
            queues = new TickerData();
            tickerQueues.put(marketOrder.getTicker(), queues);
        }
        return queues;
    }
}
