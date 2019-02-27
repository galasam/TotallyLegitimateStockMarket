package main.DataObjects;

public interface StopOrder {

    float getTriggerPrice();
    Order toNonStopOrder();

}
