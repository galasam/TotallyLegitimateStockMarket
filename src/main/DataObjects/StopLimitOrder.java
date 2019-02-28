package main.DataObjects;

import java.util.Optional;
import main.DataStructures.MarketState;

public class StopLimitOrder extends StopOrder {

    public StopLimitOrder(LimitOrder limitOrder, float triggerPrice) {
        super(limitOrder, triggerPrice);
    }

    public LimitOrder toNonStopOrder() {
        return (LimitOrder) readyOrder;
    }


    @Override
    public Optional<Trade> process(MarketState marketState) {
        marketState.getStopOrders().add(this);
        return Optional.empty();
    }
}
