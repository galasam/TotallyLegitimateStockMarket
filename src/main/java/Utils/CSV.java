package Utils;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import DataObjects.LimitOrder;
import DataObjects.MarketOrder;
import DataObjects.Order;
import DataObjects.ReadyOrder.DIRECTION;
import DataObjects.ReadyOrder.TIME_IN_FORCE;
import DataObjects.StopLimitOrder;
import DataObjects.StopMarketOrder;
import DataObjects.Trade;

public class CSV {

    private final static Map<String, Integer> INPUT_HEADINGS = new TreeMap<>();
    static {
        INPUT_HEADINGS.put("ORDER ID", 0);
        INPUT_HEADINGS.put("GROUP ID", 1);
        INPUT_HEADINGS.put("DIRECTION", 2);
        INPUT_HEADINGS.put("QUANTITY", 3);
        INPUT_HEADINGS.put("TICKER", 4);
        INPUT_HEADINGS.put("TYPE", 5);
        INPUT_HEADINGS.put("LIMIT PRICE", 6);
        INPUT_HEADINGS.put("TIME IN FORCE", 7);
        INPUT_HEADINGS.put("TRIGGER PRICE", 8);
    }
    private final static String OUTPUT_HEADER = String.join(",", "BUY ORDER", "SELL ORDER", "MATCH QTY", "MATCH PRICE");

    public static List<Order> decodeCSV(List<String> input) {
        return input.stream()
            .skip(1)
            .map(CSV::decodeCSVRow)
            .collect(Collectors.toList());
    }

    private static Order decodeCSVRow(String input) {
        final String[] values = input.split(",");

        final int orderId = Integer.parseInt(values[INPUT_HEADINGS.get("ORDER ID")]);
        final String direction = values[INPUT_HEADINGS.get("DIRECTION")];
        final int quantity = Integer.parseInt(values[INPUT_HEADINGS.get("QUANTITY")]);
        final String type = values[INPUT_HEADINGS.get("TYPE")];
        final TIME_IN_FORCE tif = TIME_IN_FORCE.valueOf(values[INPUT_HEADINGS.get("TIME IN FORCE")]);
        final String ticker = values[INPUT_HEADINGS.get("TICKER")];

        switch (type) {
            case "LIMIT":
                float limit = Float.parseFloat(values[INPUT_HEADINGS.get("LIMIT PRICE")]);
                return new LimitOrder(
                    orderId, DIRECTION.valueOf(direction), quantity, tif, ticker, limit);
            case "MARKET":
                return new MarketOrder(
                    orderId, DIRECTION.valueOf(direction), quantity, tif, ticker);
            case "STOP-LIMIT":
                limit = Float.parseFloat(values[INPUT_HEADINGS.get("LIMIT PRICE")]);
                float triggerPrice = Float.parseFloat(values[INPUT_HEADINGS.get("TRIGGER PRICE")]);
                return new StopLimitOrder(
                    new LimitOrder(orderId, DIRECTION.valueOf(direction), quantity, tif, ticker, limit),
                    triggerPrice);
            case "STOP-MARKET":
                triggerPrice = Float.parseFloat(values[INPUT_HEADINGS.get("TRIGGER PRICE")]);
                return new StopMarketOrder(
                    new MarketOrder(orderId, DIRECTION.valueOf(direction), quantity, tif, ticker),
                    triggerPrice);

            default:
                throw new UnsupportedOperationException(" Unsupported order type");
        }
    }

    public static List<String> encodeCSV(List<Trade> output) {
        return Stream.concat(
                Stream.of(OUTPUT_HEADER),
                output.stream().map(CSV::encodeCSVRow)
        ).collect(Collectors.toList());
    }

    private static String encodeCSVRow(Trade output) {
        return String.join(",",
                Integer.toString(output.getBuyOrder()),
                Integer.toString(output.getSellOrder()),
                Integer.toString(output.getMatchQuantity()),
                Float.toString(output.getMatchPrice()));
    }


}
