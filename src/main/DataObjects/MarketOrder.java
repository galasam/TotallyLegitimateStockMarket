package main.DataObjects;

public class MarketOrder extends Order{


    public MarketOrder(int orderId, DIRECTION direction, int quantity,
        TIME_IN_FORCE timeInForce, String ticker) {
        super(orderId, direction, quantity, timeInForce, ticker);
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
