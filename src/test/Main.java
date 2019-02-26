package test;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;
import main.DataObjects.Order;
import main.DataObjects.Trade;
import main.Market;
import test.Utils.CSV;
import test.Utils.File;

public class Main {

    static final String relativeDirectoryOfTestFiles = "test truths/Phase1";
    static final String absoluteDirectoryOfTestFiles = Paths.get(System.getProperty("user.dir"), relativeDirectoryOfTestFiles).toString();

    public static void main(String[] args) {
        IntStream.range(1,11).forEach(Main::runTest);
    }

    private static void runTest(int i) {
        final List<Order> orders = readOrders(i);
        final List<Trade> trades = Market.getResultingTrades(orders);
        writeTrades(i, trades);

    }

    private static List<Order> readOrders(int i) {
        final String filename = "input.test1." + Integer.toString(i);
        final String filepath = Paths.get(absoluteDirectoryOfTestFiles, filename).toString();
        final String inputText = File.readTestFile(filepath);
        return CSV.decodeCSV(inputText);
    }

    private static void writeTrades(int i, List<Trade> trades) {
        final String outputText = CSV.encodeCSV(trades);
        final String filename = "input.test1." + Integer.toString(i);
        final String filePath = Paths.get(absoluteDirectoryOfTestFiles, filename).toString();
        File.writeTestFile(filePath, outputText);
    }

}
