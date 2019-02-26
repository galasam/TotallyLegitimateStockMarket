package main.DataObjects;

public class Order {

    public int getOrderId() {
        return orderId;
    }

    public String getDirection() {
        return direction;
    }

    private final int orderId;
    private final String direction;

    public Order(int orderId, String direction) {
        this.orderId = orderId;
        this.direction = direction;
    }
}
