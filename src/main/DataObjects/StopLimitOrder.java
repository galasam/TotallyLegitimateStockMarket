package main.DataObjects;

public class StopLimitOrder extends StopOrder {

    public StopLimitOrder(LimitOrder limitOrder, float triggerPrice) {
        super(limitOrder, triggerPrice);
    }

    public LimitOrder toNonStopOrder() {
        return (LimitOrder) readyOrder;
    }


}
