package main.DataObjects;

import java.util.Optional;
import main.DataStructures.MarketState;

public class StopMarketOrder extends StopOrder {

    public StopMarketOrder(MarketOrder readyOrder, float triggerPrice) {
        super(readyOrder, triggerPrice);
    }

    public MarketOrder toNonStopOrder() {
        return (MarketOrder) getReadyOrder();
    }

    @Override
    public String toString() {
        return "StopMarketOrder{" +
            "triggerPrice=" + triggerPrice +
            ", readyOrder=" + readyOrder +
            '}';
    }

}
