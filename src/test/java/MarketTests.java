import dataObjects.LimitOrder;
import dataObjects.MarketOrder;
import dataObjects.ReadyOrder.DIRECTION;
import dataObjects.ReadyOrder.TIME_IN_FORCE;
import dataObjects.Trade;
import main.Market;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class MarketTests {

    int newOrderID = 1;

    @Test
    public void testSimpleTimeStep() {

        Market market = new Market();

        LimitOrder limitOrder = LimitOrder.builder()
            .orderId(1)
            .direction(DIRECTION.BUY)
            .quantity(999)
            .ticker("Fred")
            .limit(3.14f)
            .timeInForce(TIME_IN_FORCE.GTC)
            .build();

        MarketOrder marketOrder = MarketOrder.builder()
            .orderId(2)
            .direction(DIRECTION.SELL)
            .quantity(999)
            .ticker("Fred")
            .timeInForce(TIME_IN_FORCE.GTC)
            .build();

        Trade tradeOutputTest = Trade.builder()
            .buyOrder(1)
            .sellOrder(2)
            .matchQuantity(999)
            .matchPrice(3.14f)
            .build();

        market.completeTimestep(limitOrder);
        market.completeTimestep(marketOrder);
        List<Trade> trades = market.getAllResultingTrades();

        Assert.assertTrue("Should be able to match a buy limit and sell market order", trades.size() == 1);
        Assert.assertTrue("Should match should be correct", trades.get(0).equals(tradeOutputTest));
    }

    @Test
    public void testTimeStepWithMatchingLimits() {

        Market market = new Market();

        LimitOrder limitOrderA = LimitOrder.builder()
            .orderId(1)
            .direction(DIRECTION.BUY)
            .quantity(999)
            .ticker("Fred")
            .limit(3.14f)
            .timeInForce(TIME_IN_FORCE.GTC)
            .build();

        LimitOrder limitOrderBMatchingA = LimitOrder.builder()
            .orderId(2)
            .direction(DIRECTION.SELL)
            .quantity(999)
            .ticker("Fred")
            .limit(2f)
            .timeInForce(TIME_IN_FORCE.GTC)
            .build();

        Trade tradeOutputTest = Trade.builder()
            .buyOrder(limitOrderA.getOrderId())
            .sellOrder(limitOrderBMatchingA.getOrderId())
            .matchQuantity(999)
            .matchPrice(3.14f)
            .build();

        market.completeTimestep(limitOrderA);
        market.completeTimestep(limitOrderBMatchingA);
        List<Trade> trades = market.getAllResultingTrades();

        Assert.assertTrue("Should be able to match a buy and sell matching limit orders",trades.size() == 1);
        Assert.assertTrue("Should match should be correct", trades.get(0).equals(tradeOutputTest));
    }


    @Test
    public void testTimeStepWithNonMatchingLimits() {

        Market market = new Market();

        LimitOrder limitOrderA = LimitOrder.builder()
            .orderId(1)
            .direction(DIRECTION.BUY)
            .quantity(999)
            .ticker("Fred")
            .limit(3.14f)
            .timeInForce(TIME_IN_FORCE.GTC)
            .build();

        LimitOrder limitOrderBNotMatchingA = LimitOrder.builder()
            .orderId(2)
            .direction(DIRECTION.SELL)
            .quantity(999)
            .ticker("Fred")
            .limit(10f)
            .timeInForce(TIME_IN_FORCE.GTC)
            .build();

        market.completeTimestep(limitOrderA);
        market.completeTimestep(limitOrderBNotMatchingA);
        List<Trade> trades = market.getAllResultingTrades();

        Assert.assertTrue("Should not match a buy and sell non-matching limit orders",
            trades.size() == 0);
    }


}
