package main.DataObjects;

public class LimitOrder extends Order {

    public float getLimit() {
        return limit;
    }

    private float limit;

    public LimitOrder(int orderId, String direction, float limit) {
        super(orderId, direction);
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "LimitOrder{" +
            "limit=" + limit +
            ", orderId=" + orderId +
            ", direction='" + direction + '\'' +
            '}';
    }
}
