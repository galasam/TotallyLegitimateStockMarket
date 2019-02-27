package main.DataObjects;

public class Order {

    public enum DIRECTION {SELL, BUY};

    public int getOrderId() {
        return orderId;
    }

    public DIRECTION getDirection() {
        return direction;
    }

    protected final int orderId;
    protected final DIRECTION direction;

    public Order(int orderId, DIRECTION direction) {
        this.orderId = orderId;
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "Order{" +
            "orderId=" + orderId +
            ", direction='" + direction.toString() + '\'' +
            '}';
    }
}
