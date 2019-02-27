package main.DataObjects;

public class MarketOrder extends Order{

    public MarketOrder(int orderId, DIRECTION direction, int quantity) {
        super(orderId, direction, quantity);
    }

    @Override
    public String toString() {
        return "MarketOrder{" +
            "orderId=" + orderId +
            ", direction=" + direction +
            ", quantity=" + quantity +
            '}';
    }
}
