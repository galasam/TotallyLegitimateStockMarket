package main.DataObjects;

import java.util.Optional;
import java.util.logging.Logger;
import main.DataObjects.ReadyOrder.DIRECTION;
import main.DataStructures.MarketState;

public abstract class StopOrder extends Order {

    final private static Logger LOGGER = Logger.getLogger("MARKET_LOGGER");

    float triggerPrice;
    ReadyOrder readyOrder;

    StopOrder(ReadyOrder readyOrder, float triggerPrice) {
        this.triggerPrice = triggerPrice;
        this.readyOrder = readyOrder;
    }

    private float getTriggerPrice() {
        return triggerPrice;
    }

    public ReadyOrder getReadyOrder() {
        return readyOrder;
    }

    public boolean isTriggered(MarketState marketState) {
        ReadyOrder readyOrder = getReadyOrder();
        Optional<Float> lastExec = marketState.getTickerQueueGroup(readyOrder).getLastExecutedTradePrice();
        LOGGER.finest("Checking if there has been a previous trade");
        if(lastExec.isPresent()) {
            LOGGER.finest("Previous trade found, checking direction");
            if(readyOrder.getDirection().equals(DIRECTION.BUY)) {
                LOGGER.finest("Buy direction: testing trigger");
                return getTriggerPrice() <= lastExec.get();
            } else if(readyOrder.getDirection().equals(DIRECTION.SELL)) {
                LOGGER.finest("Sell direction: testing trigger");
                return getTriggerPrice() >= lastExec.get();
            } else {
                throw new UnsupportedOperationException("Order direction not supported");
            }
        } else {
            LOGGER.finest("No previous trade found");
            return false;
        }
    }
}
