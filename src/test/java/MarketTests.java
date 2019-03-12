import DataObjects.LimitOrder;
import DataObjects.MarketOrder;
import DataObjects.ReadyOrder.DIRECTION;
import DataObjects.ReadyOrder.TIME_IN_FORCE;
import DataObjects.Trade;
import Main.Market;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class MarketTests {

    int newOrderID = 1;

    LimitOrder limitOrderA = LimitOrder.builder()
        .orderId(newOrderID++)
        .direction(DIRECTION.BUY)
        .quantity(999)
        .ticker("Fred")
        .limit(3.14f)
        .timeInForce(TIME_IN_FORCE.GTC)
        .build();

    MarketOrder marketOrderA = MarketOrder.builder()
        .orderId(newOrderID++)
        .direction(DIRECTION.SELL)
        .quantity(999)
        .ticker("Fred")
        .timeInForce(TIME_IN_FORCE.GTC)
        .build();

    Trade simpleLimitMarketTrade = Trade.builder()
        .buyOrder(limitOrderA.getOrderId())
        .sellOrder(marketOrderA.getOrderId())
        .matchQuantity(999)
        .matchPrice(3.14f)
        .build();

    @Test
    public void testSimpleTimeStep() {
        Market market = new Market();
        market.completeTimestep(limitOrderA);
        market.completeTimestep(marketOrderA);
        List<Trade> trades = market.getAllResultingTrades();
        Assert.assertTrue("Should be able to match a buy limit and sell market order",
            trades.size() == 1 && trades.get(0).equals(simpleLimitMarketTrade));
    }

    LimitOrder limitOrderBMatchingA = LimitOrder.builder()
        .orderId(newOrderID++)
        .direction(DIRECTION.SELL)
        .quantity(999)
        .ticker("Fred")
        .limit(2f)
        .timeInForce(TIME_IN_FORCE.GTC)
        .build();

    Trade matchingLimitLimitTrade = Trade.builder()
        .buyOrder(limitOrderA.getOrderId())
        .sellOrder(limitOrderBMatchingA.getOrderId())
        .matchQuantity(999)
        .matchPrice(3.14f)
        .build();

    @Test
    public void testTimeStepWithMatchingLimits() {
        Market market = new Market();
        market.completeTimestep(limitOrderA);
        market.completeTimestep(limitOrderBMatchingA);
        List<Trade> trades = market.getAllResultingTrades();
        Assert.assertTrue("Should be able to match a buy and sell matching limit orders",
            trades.size() == 1 && trades.get(0).equals(matchingLimitLimitTrade));
    }

    LimitOrder limitOrderCNotMatchingA = LimitOrder.builder()
        .orderId(newOrderID++)
        .direction(DIRECTION.SELL)
        .quantity(999)
        .ticker("Fred")
        .limit(10f)
        .timeInForce(TIME_IN_FORCE.GTC)
        .build();

    @Test
    public void testTimeStepWithNonMatchingLimits() {
        Market market = new Market();
        market.completeTimestep(limitOrderA);
        market.completeTimestep(limitOrderCNotMatchingA);
        List<Trade> trades = market.getAllResultingTrades();
        Assert.assertTrue("Should not match a buy and sell non-matching limit orders",
            trades.size() == 0);
    }


}
