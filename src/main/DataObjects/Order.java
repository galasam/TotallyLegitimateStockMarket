package main.DataObjects;

public class Order {

    public int getOrderId() {
        return orderId;
    }

    public String getDirection() {
        return direction;
    }

    protected final int orderId;
    protected final String direction;

    public Order(int orderId, String direction) {
        this.orderId = orderId;
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "Order{" +
            "orderId=" + orderId +
            ", direction='" + direction + '\'' +
            '}';
    }
}
