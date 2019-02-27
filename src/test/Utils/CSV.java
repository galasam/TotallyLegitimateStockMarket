package test.Utils;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import main.DataObjects.LimitOrder;
import main.DataObjects.MarketOrder;
import main.DataObjects.Order;
import main.DataObjects.Order.DIRECTION;
import main.DataObjects.Trade;

public class CSV {

    final static String OUTPUT_HEADER = String.join(",", "BUY ORDER", "SELL ORDER", "MATCH QTY", "MATCH PRICE");

    public static List<Order> decodeCSV(List<String> input) {
        return input.stream()
            .skip(1)
            .map(CSV::decodeCSVRow)
            .collect(Collectors.toList());
    }

    private static Order decodeCSVRow(String input) {
        final String[] values = input.split(",");

        final int orderId = Integer.parseInt(values[0]);
        final String direction = values[2];
        final String type = values[5];

        if(type.equals("LIMIT")) {
            final float limit = Float.parseFloat(values[6]);
            return new LimitOrder(orderId, DIRECTION.valueOf(direction),limit);
        } else if (type.equals("MARKET")) {
            return new MarketOrder(orderId, DIRECTION.valueOf(direction));
        } else {
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
