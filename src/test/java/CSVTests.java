import DataObjects.LimitOrder;
import DataObjects.Order;
import DataObjects.ReadyOrder.DIRECTION;
import DataObjects.ReadyOrder.TIME_IN_FORCE;
import DataObjects.StopOrder;
import DataObjects.Trade;
import Utils.CSV;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class CSVTests {

    private final static String csvInputHeader = "ORDER ID,GROUP ID,DIRECTION,QUANTITY,TICKER,TYPE,LIMIT PRICE,TIME IN FORCE,TRIGGER PRICE";

    private final static String limitOrderInput = "42,1,BUY,999,Fred,STOP-LIMIT,3.14,GTC,666";
    private final static Order limitOrderOutput = StopOrder.builder().triggerPrice(666)
        .readyOrder(LimitOrder.builder()
            .orderId(42)
            .direction(DIRECTION.BUY)
            .quantity(999)
            .ticker("Fred")
            .limit(3.14f)
            .timeInForce(TIME_IN_FORCE.GTC)
            .build()
        ).build();

    private final static Trade tradeInput = Trade.builder()
        .buyOrder(64)
        .sellOrder(118)
        .matchPrice(1.5f)
        .matchQuantity(451)
        .build();

    private final static String csvOutputHeader = "BUY ORDER,SELL ORDER,MATCH QTY,MATCH PRICE";
    private final static String tradeOutput = "64,118,451,1.5";

    @Test
    public void canDecodeCSVStopLimitOrder() {
        final List<String> csvInput = new ArrayList<>();
        csvInput.add(csvInputHeader);
        csvInput.add(limitOrderInput);
        List<Order> orders = CSV.decodeCSV(csvInput);
        Assert.assertTrue("Decoder should decode an stop limit order correctly", orders.size() == 1 && orders.get(0).equals(limitOrderOutput));
    }

    @Test
    public void canEncodeTradeToCSV() {
        final List<String> outputTest = new ArrayList<>();
        outputTest.add(csvOutputHeader);
        outputTest.add(tradeOutput);
        final List<Trade> inputTrades = new LinkedList<>();
        inputTrades.add(tradeInput);
        final List<String> output = CSV.encodeCSV(inputTrades);
        Assert.assertTrue("Encoder should encode a trade correctly", output.equals(outputTest));
    }

}
