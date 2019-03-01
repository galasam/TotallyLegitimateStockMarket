package main.DataObjects;

public class StopMarketOrder extends StopOrder {

    public StopMarketOrder(MarketOrder readyOrder, float triggerPrice) {
        super(readyOrder, triggerPrice);
    }

    @Override
    public String toString() {
        return "StopMarketOrder{" +
            "triggerPrice=" + triggerPrice +
            ", readyOrder=" + readyOrder +
            '}';
    }

}
