package main.DataStructures;

import java.util.SortedSet;
import java.util.TreeSet;
import main.DataObjects.LimitOrder;
import main.DataObjects.MarketOrder;
import main.DataStructures.LimitOrderQueue.SORTING_METHOD;

public class TickerQueueGroup {
    private final SortedSet<LimitOrder> sellLimitOrders = new LimitOrderQueue(SORTING_METHOD.PRICE_ASC);
    private final SortedSet<LimitOrder> buyLimitOrders = new LimitOrderQueue(SORTING_METHOD.PRICE_DECS);
    private final SortedSet<MarketOrder> buyMarketOrders = new TreeSet<>((a, b) -> a.getOrderId() - b.getOrderId());
    private final SortedSet<MarketOrder> sellMarketOrders = new TreeSet<>((a, b) -> a.getOrderId() - b.getOrderId());

    public SortedSet<LimitOrder> getSellLimitOrders() {
        return sellLimitOrders;
    }

    public SortedSet<LimitOrder> getBuyLimitOrders() {
        return buyLimitOrders;
    }

    public SortedSet<MarketOrder> getBuyMarketOrders() {
        return buyMarketOrders;
    }

    public SortedSet<MarketOrder> getSellMarketOrders() {
        return sellMarketOrders;
    }

    @Override
    public String toString() {
        return "TickerQueueGroup{" +
            "sellLimitOrders=" + sellLimitOrders +
            ", buyLimitOrders=" + buyLimitOrders +
            ", buyMarketOrders=" + buyMarketOrders +
            ", sellMarketOrders=" + sellMarketOrders +
            '}';
    }
}
