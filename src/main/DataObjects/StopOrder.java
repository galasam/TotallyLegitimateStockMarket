package main.DataObjects;

import java.util.Optional;
import java.util.logging.Logger;
import main.DataObjects.ReadyOrder.DIRECTION;
import main.DataStructures.MarketState;
import main.TradePriceSubscriber;

public abstract class StopOrder extends Order implements TradePriceSubscriber{

    final private static Logger LOGGER = Logger.getLogger("MARKET_LOGGER");

    float triggerPrice;
    ReadyOrder readyOrder;
    boolean triggerSatisfied;

    StopOrder(ReadyOrder readyOrder, float triggerPrice) {
        this.triggerPrice = triggerPrice;
        this.readyOrder = readyOrder;
        this.triggerSatisfied = false;
    }

    private float getTriggerPrice() {
        return triggerPrice;
    }

    public ReadyOrder getReadyOrder() {
        return readyOrder;
    }

    public void updateIsTriggerSatisfied(MarketState marketState) {
        ReadyOrder readyOrder = getReadyOrder();
        Optional<Float> lastExec = marketState.getTickerQueueGroup(readyOrder).getLastExecutedTradePrice();
        LOGGER.finest("Checking if there has been a previous trade");
        if(lastExec.isPresent()) {
            LOGGER.finest("Previous trade found");
            updateIsTriggerSatisfied(lastExec.get());
        } else {
            LOGGER.finest("No previous trade found");
        }
    }

    public boolean isTriggered() {
        return triggerSatisfied;
    }

    @Override
    public void notify(float tradePrice) {
        LOGGER.finer("Stop order notified of last exec trade price: " + tradePrice);
        updateIsTriggerSatisfied(tradePrice);
    }

    private void updateIsTriggerSatisfied(float tradePrice) {
        ReadyOrder readyOrder = getReadyOrder();
        LOGGER.finest("Checking direction");
        if(readyOrder.getDirection().equals(DIRECTION.BUY)) {
            LOGGER.finest("Buy direction: testing trigger");
            triggerSatisfied =  getTriggerPrice() <= tradePrice;
        } else if(readyOrder.getDirection().equals(DIRECTION.SELL)) {
            LOGGER.finest("Sell direction: testing trigger");
            triggerSatisfied =  getTriggerPrice() >= tradePrice;
        } else {
            throw new UnsupportedOperationException("Order direction not supported");
        }
        LOGGER.finest("TriggerSatisfied: " + Boolean.toString(triggerSatisfied));
    }

    @Override
    public Optional<Trade> process(MarketState marketState) {
        updateIsTriggerSatisfied(marketState);
        marketState.getTickerQueueGroup(getReadyOrder()).subscribeToTradePriceChanges(this);
        marketState.getStopOrders().add(this);
        return Optional.empty();
    }
}
