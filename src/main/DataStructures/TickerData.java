package main.DataStructures;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.SortedSet;
import java.util.stream.Collectors;
import main.DataObjects.LimitOrder;
import main.DataObjects.MarketOrder;
import main.DataStructures.LimitOrderQueue.SORTING_METHOD;
import main.TradePriceSubscriber;

public class TickerData {
    private final SortedSet<LimitOrder> sellLimitOrders = new LimitOrderQueue(SORTING_METHOD.PRICE_ASC);
    private final SortedSet<LimitOrder> buyLimitOrders = new LimitOrderQueue(SORTING_METHOD.PRICE_DECS);
    private final Queue<MarketOrder> buyMarketOrders = new LinkedList<>();
    private final Queue<MarketOrder> sellMarketOrders = new LinkedList<>();

    private Optional<Float> lastExecutedTradePrice = Optional.empty();

    private List<TradePriceSubscriber> tradePriceSubscribers = new LinkedList<>();

    public void subscribeToTradePriceChanges(TradePriceSubscriber subscriber) {
        tradePriceSubscribers.add(subscriber);
    }

    public Optional<Float> getLastExecutedTradePrice() {
        return lastExecutedTradePrice;
    }

    public void setLastExecutedTradePrice(float lastExecutedTradePrice) {
        this.lastExecutedTradePrice = Optional.of(lastExecutedTradePrice);
        tradePriceSubscribers = tradePriceSubscribers.stream()
            .filter(x -> !x.notify(lastExecutedTradePrice))
            .collect(Collectors.toList());
    }

    public SortedSet<LimitOrder> getSellLimitOrders() {
        return sellLimitOrders;
    }

    public SortedSet<LimitOrder> getBuyLimitOrders() {
        return buyLimitOrders;
    }

    public Queue<MarketOrder> getBuyMarketOrders() {
        return buyMarketOrders;
    }

    public Queue<MarketOrder> getSellMarketOrders() {
        return sellMarketOrders;
    }

    @Override
    public String toString() {
        return "TickerData{" +
            "sellLimitOrders=" + sellLimitOrders +
            ", buyLimitOrders=" + buyLimitOrders +
            ", buyMarketOrders=" + buyMarketOrders +
            ", sellMarketOrders=" + sellMarketOrders +
            ", lastExecutedTradePrice=" + lastExecutedTradePrice +
            '}';
    }
}
