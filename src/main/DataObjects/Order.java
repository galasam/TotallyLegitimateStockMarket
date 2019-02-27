package main.DataObjects;

public class Order {

    public enum DIRECTION {SELL, BUY};

    public int getOrderId() {
        return orderId;
    }

    public DIRECTION getDirection() {
        return direction;
    }

    public int getQuantity() {
        return quantity;
    }

    protected final int orderId;
    protected final DIRECTION direction;
    protected final int quantity;

    public Order(int orderId, DIRECTION direction, int quantity) {
        this.orderId = orderId;
        this.direction = direction;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Order{" +
            "orderId=" + orderId +
            ", direction='" + direction.toString() + '\'' +
            '}';
    }
}
