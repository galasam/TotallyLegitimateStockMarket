package main.DataObjects;

import java.util.Optional;
import java.util.Queue;
import java.util.logging.Logger;
import main.DataObjects.ReadyOrder.DIRECTION;
import main.DataStructures.MarketState;
import main.TradePriceSubscriber;

public abstract class StopOrder extends Order implements TradePriceSubscriber{

    final private static Logger LOGGER = Logger.getLogger("MARKET_LOGGER");

    float triggerPrice;
    ReadyOrder readyOrder;

    private Queue<ReadyOrder> triggeredOrderBacklog;

    StopOrder(ReadyOrder readyOrder, float triggerPrice) {
        this.triggerPrice = triggerPrice;
        this.readyOrder = readyOrder;
    }

    private float getTriggerPrice() {
        return triggerPrice;
    }

    private ReadyOrder getReadyOrder() {
        return readyOrder;
    }

    @Override
    public boolean notify(float tradePrice) {
        LOGGER.finer("Stop order notified of last exec trade price: " + tradePrice);
        LOGGER.finer("Checking if trigger is satisfied");
        if(isTriggerSatisfied(tradePrice)) {
            LOGGER.finer("Trigger is satisfied. Adding to queue");
            triggeredOrderBacklog.add(getReadyOrder());
            return true;
        } else {
            LOGGER.finer("Trigger is not satisfied");
            return false;
        }
    }

    private boolean isTriggerSatisfied(float tradePrice) {
        ReadyOrder readyOrder = getReadyOrder();
        LOGGER.finest("Checking direction");
        if(readyOrder.getDirection().equals(DIRECTION.BUY)) {
            LOGGER.finest("Buy direction: testing trigger");
            return getTriggerPrice() <= tradePrice;
        } else if(readyOrder.getDirection().equals(DIRECTION.SELL)) {
            LOGGER.finest("Sell direction: testing trigger");
            return getTriggerPrice() >= tradePrice;
        } else {
            throw new UnsupportedOperationException("Order direction not supported");
        }
    }

    private boolean isTriggerSatisfied(MarketState marketState) {
        ReadyOrder readyOrder = getReadyOrder();
        Optional<Float> lastExec = marketState.getTickerQueueGroup(readyOrder)
            .getLastExecutedTradePrice();
        LOGGER.finest("Checking if there has been a previous trade");
        if(lastExec.isPresent()) {
            LOGGER.finest("Previous trade found");
            return isTriggerSatisfied(lastExec.get());
        } else {
            LOGGER.finest("No previous trade found");
            return false;
        }
    }

    @Override
    public Optional<Trade> process(MarketState marketState) {
        if(isTriggerSatisfied(marketState)) {
            marketState.getTriggeredOrderBacklog().add(getReadyOrder());
        } else {
            marketState.getTickerQueueGroup(getReadyOrder())
                .subscribeToTradePriceChanges(this);
            marketState.getStopOrders().add(this);
            triggeredOrderBacklog = marketState.getTriggeredOrderBacklog();
        }
        return Optional.empty();
    }
}
