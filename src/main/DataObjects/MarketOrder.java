package main.DataObjects;

public class MarketOrder extends Order{

    public MarketOrder(int orderId, DIRECTION direction, int quantity) {
        super(orderId, direction, quantity);
    }
}
