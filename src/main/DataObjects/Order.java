package main.DataObjects;

public class Order {

    public enum DIRECTION {SELL, BUY};
    public enum TIME_IN_FORCE {FOK, GTC};

    public int getOrderId() {
        return orderId;
    }

    public DIRECTION getDirection() {
        return direction;
    }

    public int getQuantity() {
        return quantity;
    }

    public TIME_IN_FORCE getTimeInForce() {
        return timeInForce;
    }

    protected final int orderId;
    protected final DIRECTION direction;
    protected final int quantity;
    protected final TIME_IN_FORCE timeInForce;
    protected final String ticker;

    public Order(int orderId, DIRECTION direction, int quantity,
        TIME_IN_FORCE timeInForce, String ticker) {
        this.orderId = orderId;
        this.direction = direction;
        this.quantity = quantity;
        this.timeInForce = timeInForce;
        this.ticker = ticker;
    }

    @Override
    public String toString() {
        return "Order{" +
            "orderId=" + orderId +
            ", direction=" + direction +
            ", quantity=" + quantity +
            ", timeInForce=" + timeInForce +
            '}';
    }
}
