import static Utils.MarketUtils.queueIfTimeInForce;

import DataObjects.LimitOrder;
import DataObjects.ReadyOrder.TIME_IN_FORCE;
import java.util.LinkedList;
import java.util.List;

public class MarketUtilTests {

    public void testQueueIfTimeInForce() {
        List<LimitOrder> orders = new LinkedList<>();
        LimitOrder order = LimitOrder.builder().timeInForce(TIME_IN_FORCE.GTC).build();
        queueIfTimeInForce(order, orders);
    }

}
