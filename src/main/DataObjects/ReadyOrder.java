package main.DataObjects;

public abstract class ReadyOrder extends Order {
    public enum DIRECTION {SELL, BUY}
    public enum TIME_IN_FORCE {FOK, GTC}

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

    public String getTicker() {
        return ticker;
    }

    final int orderId;
    protected final DIRECTION direction;
    protected final int quantity;
    final TIME_IN_FORCE timeInForce;
    protected final String ticker;

    ReadyOrder(int orderId, DIRECTION direction, int quantity,
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
