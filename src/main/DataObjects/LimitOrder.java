package main.DataObjects;

import static main.MarketUtils.constructTrade;
import static main.MarketUtils.queueIfTimeInForce;

import java.util.Optional;
import java.util.SortedSet;
import java.util.logging.Logger;
import main.DataStructures.MarketState;
import main.DataStructures.TickerData;

public class LimitOrder extends ReadyOrder {

    final private static Logger LOGGER = Logger.getLogger("MARKET_LOGGER");

    public float getLimit() {
        return limit;
    }

    float limit;

    public LimitOrder(int orderId, DIRECTION direction, int quantity, TIME_IN_FORCE timeInForce, String ticker, float limit) {
        super(orderId, direction, quantity, timeInForce, ticker);
        this.limit = limit;
    }

    public boolean limitMatches(LimitOrder other) {
        if(getDirection().equals(DIRECTION.BUY)) {
            return getLimit() >= other.getLimit();
        } else if(getDirection().equals(DIRECTION.SELL)) {
            return getLimit() <= other.getLimit();
        } else {
            throw new UnsupportedOperationException("Order direction not supported");
        }
    }

    @Override
    public Optional<Trade> process(MarketState marketState) {
        TickerData tickerData = marketState.getTickerQueueGroup(this);
        if (getDirection() == DIRECTION.BUY) {
            return processDirectedLimitOrder(tickerData,
                tickerData.getSellMarketOrders(),
                tickerData.getBuyLimitOrders(),
                tickerData.getSellLimitOrders());
        } else if (getDirection() == DIRECTION.SELL) {
            return processDirectedLimitOrder(tickerData,
                tickerData.getBuyMarketOrders(),
                tickerData.getSellLimitOrders(),
                tickerData.getBuyLimitOrders());
        } else {
            throw new UnsupportedOperationException("Order direction not supported");
        }
    }

    private Optional<Trade> processDirectedLimitOrder(TickerData tickerData,
        SortedSet<MarketOrder> marketOrders,
        SortedSet<LimitOrder> sameTypeLimitOrders,
        SortedSet<LimitOrder> oppositeTypeLimitOrders) {
        LOGGER.finest("Checking Market Order queue");
        if(marketOrders.isEmpty()) {
            LOGGER.finest("Market Order queue empty, so checking Limit orders");
            if(oppositeTypeLimitOrders.isEmpty()) {
                LOGGER.finest("Limit Order queue empty, so check if time in force");
                queueIfTimeInForce(this, sameTypeLimitOrders);
                return Optional.empty();
            } else {
                LimitOrder otherLimitOrder = oppositeTypeLimitOrders.first();
                LOGGER.finest("Limit Order queue not empty, so checking if best order matches: " + otherLimitOrder.toString());

                if(limitMatches(otherLimitOrder)) {
                    LOGGER.finest("Limits match so completing trade");
                    oppositeTypeLimitOrders.remove(otherLimitOrder);
                    return Optional.of(constructTrade(this, otherLimitOrder, otherLimitOrder.getLimit(), tickerData));
                } else {
                    LOGGER.finest("Limits do not match, so check if time in force");
                    queueIfTimeInForce(this, sameTypeLimitOrders);
                    return Optional.empty();
                }
            }
        } else {
            LOGGER.finest("Market Order queue not empty, so trading with oldest order: " + toString());
            MarketOrder marketOrder = marketOrders.first();
            marketOrders.remove(marketOrder);
            return Optional.of(constructTrade(marketOrder, this, getLimit(), tickerData));

        }
    }

    @Override
    public String toString() {
        return "LimitOrder{" +
            "limit=" + limit +
            ", orderId=" + orderId +
            ", direction=" + direction +
            ", quantity=" + quantity +
            ", timeInForce=" + timeInForce +
            '}';
    }


}
