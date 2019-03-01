package main.DataObjects;

import static main.MarketUtils.constructTrade;
import static main.MarketUtils.getIfGTC;

import java.util.Optional;
import java.util.SortedSet;
import java.util.logging.Logger;
import main.DataStructures.MarketState;
import main.DataStructures.TickerData;

public class MarketOrder extends ReadyOrder{

    final private static Logger LOGGER = Logger.getLogger("MARKET_LOGGER");

    public MarketOrder(int orderId, DIRECTION direction, int quantity,
        TIME_IN_FORCE timeInForce, String ticker) {
        super(orderId, direction, quantity, timeInForce, ticker);
    }

    public Optional<Trade> process(MarketState marketState) {
        TickerData tickerData = marketState.getTickerQueueGroup(this);
        if (getDirection() == DIRECTION.BUY) {
            return processDirectedMarketOrder(this, tickerData,
                tickerData.getSellLimitOrders(),tickerData.getBuyMarketOrders());
        } else if (getDirection() == DIRECTION.SELL) {
            return processDirectedMarketOrder(this, tickerData,
                tickerData.getBuyLimitOrders(), tickerData.getSellMarketOrders());
        } else {
            throw new UnsupportedOperationException("Order direction not supported");
        }
    }



    private Optional<Trade> processDirectedMarketOrder(MarketOrder marketOrder, TickerData tickerData,
        SortedSet<LimitOrder> limitOrders, SortedSet<MarketOrder> marketOrders) {
        LOGGER.finest("Checking Limit Order queue");
        if(limitOrders.isEmpty()) {
            LOGGER.finest("Limit Order queue empty, so check if time in force");
            getIfGTC(this).ifPresent(marketOrders::add);
            return Optional.empty();
        } else {
            LimitOrder limitOrder = limitOrders.first();
            LOGGER.finest("Limit Order queue not empty, so trading with best limit order: " + limitOrder.toString());
            limitOrders.remove(limitOrder);
            return Optional.of(constructTrade(marketOrder, limitOrder, limitOrder.getLimit(), tickerData));
        }
    }

    @Override
    public String toString() {
        return "Order{" +
            "orderId=" + orderId +
            ", direction=" + direction +
            ", quantity=" + quantity +
            ", timeInForce=" + timeInForce +
            '}';
    }
}
