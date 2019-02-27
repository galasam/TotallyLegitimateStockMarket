package main.DataObjects;

import com.sun.org.apache.xalan.internal.utils.XMLSecurityManager.Limit;

public class StopLimitOrder extends LimitOrder implements StopOrder {

    private float triggerPrice;

    public StopLimitOrder(int orderId, DIRECTION direction, int quantity,
        TIME_IN_FORCE timeInForce, String ticker, float triggerPrice, float limit) {
        super(orderId, direction, quantity, timeInForce, ticker, limit);
        this.triggerPrice = triggerPrice;
    }

    @Override
    public float getTriggerPrice() {
        return triggerPrice;
    }

    @Override
    public Order toNonStopOrder() {
        return new LimitOrder(orderId, direction, quantity, timeInForce, ticker, limit);
    }
}
