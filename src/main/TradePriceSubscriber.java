package main;

public interface TradePriceSubscriber {
    boolean notify(float tradePrice);
}
