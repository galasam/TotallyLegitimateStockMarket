package DataObjects;

public abstract class StopOrder extends Order {

    float triggerPrice;
    ReadyOrder readyOrder;

    StopOrder(ReadyOrder readyOrder, float triggerPrice) {
        this.triggerPrice = triggerPrice;
        this.readyOrder = readyOrder;
    }

    public float getTriggerPrice() {
        return triggerPrice;
    }

    public ReadyOrder getReadyOrder() {
        return readyOrder;
    }

    public Order toNonStopOrder() {
        return readyOrder;
    }

}
