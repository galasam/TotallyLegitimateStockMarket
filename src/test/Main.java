package test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;
import main.DataObjects.Order;
import main.DataObjects.Trade;
import main.Market;
import test.Utils.CSV;
import test.Utils.File;

public class Main {

    private static final String relativeDirectoryOfTestFiles = "test truths/Phase1";
    private static final String absoluteDirectoryOfTestFiles = Paths.get(System.getProperty("user.dir"), relativeDirectoryOfTestFiles).toString();


    public static void main(String[] args) {
        IntStream.range(1,11).forEach(Main::runTest);
    }

    private static void runTest(int i) {
        try {
            final List<Order> orders = readOrders(i);
            final List<Trade> trades = Market.getResultingTrades(orders);
            writeTrades(i, trades);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Order> readOrders(int i) throws IOException {
        final String filename = "input.test1." + Integer.toString(i) + ".csv";
        final String filepath = Paths.get(absoluteDirectoryOfTestFiles, filename).toString();
        final List<String> inputText = File.readTestFile(filepath);
        return CSV.decodeCSV(inputText);
    }

    private static void writeTrades(int i, List<Trade> trades)
        throws FileNotFoundException, UnsupportedEncodingException {
        final List<String> outputText = CSV.encodeCSV(trades);
        final String filename = "input.test1." + Integer.toString(i) + ".csv";
        final String filePath = Paths.get(absoluteDirectoryOfTestFiles, filename).toString();
        File.writeTestFile(filePath, outputText);
    }

}
