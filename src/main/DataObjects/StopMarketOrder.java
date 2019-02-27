package main.DataObjects;

public class StopMarketOrder extends MarketOrder implements StopOrder{

    private float triggerPrice;

    public StopMarketOrder(int orderId, DIRECTION direction, int quantity,
        TIME_IN_FORCE timeInForce, String ticker, float triggerPrice) {
        super(orderId, direction, quantity, timeInForce, ticker);
        this.triggerPrice = triggerPrice;
    }

    @Override
    public float getTriggerPrice() {
        return triggerPrice;
    }

    @Override
    public Order toNonStopOrder() {
        return new MarketOrder(orderId, direction, quantity, timeInForce, ticker);
    }
}
