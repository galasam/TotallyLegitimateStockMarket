package main.DataObjects;

public class LimitOrder extends ReadyOrder {

    public float getLimit() {
        return limit;
    }

    float limit;

    public LimitOrder(int orderId, DIRECTION direction, int quantity, TIME_IN_FORCE timeInForce, String ticker, float limit) {
        super(orderId, direction, quantity, timeInForce, ticker);
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "LimitOrder{" +
            "limit=" + limit +
            ", orderId=" + orderId +
            ", direction=" + direction +
            ", quantity=" + quantity +
            ", timeInForce=" + timeInForce +
            '}';
    }
}
