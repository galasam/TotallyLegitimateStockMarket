package main.DataObjects;

public class LimitOrder extends Order {

    public float getLimit() {
        return limit;
    }

    private float limit;

    public LimitOrder(int orderId, DIRECTION direction, int quantity, TIME_IN_FORCE timeInForce, float limit) {
        super(orderId, direction, quantity, timeInForce);
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
