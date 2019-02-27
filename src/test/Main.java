package test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import main.DataObjects.Order;
import main.DataObjects.Trade;
import main.MarketManager;
import test.Utils.CSV;
import test.Utils.File;

public class Main {

    final private static Logger LOGGER = Logger.getLogger("MARKET_LOGGER");

    private static final String relativeDirectoryOfTestFiles = "test truths/Phase1";
    private static final String absoluteDirectoryOfTestFiles = Paths.get(System.getProperty("user.dir"), relativeDirectoryOfTestFiles).toString();

    private static void setupLogger() {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        LOGGER.addHandler(consoleHandler);
        LOGGER.setLevel(Level.INFO);
    }

    public static void main(String[] args) {
        setupLogger();
        LOGGER.info("Running All Tests in: " + relativeDirectoryOfTestFiles);
        IntStream.range(1,12).forEach(Main::runTest);
        //runTest(8);
    }

    private static void runTest(int i) {
        try {
            LOGGER.fine(String.format("Running test %d", i));
            final List<Order> orders = readOrders(i);
            final List<Trade> trades = MarketManager.getResultingTrades(orders);
            writeTrades(i, trades);
        } catch (IOException e) {
            LOGGER.warning("Could not run test due to IO Exception");
            e.printStackTrace();
        }
    }

    private static List<Order> readOrders(int i) throws IOException {
        LOGGER.fine("Reading Orders from file");
        final String filename = "input.test1." + Integer.toString(i) + ".csv";
        final String filepath = Paths.get(absoluteDirectoryOfTestFiles, filename).toString();
        final List<String> inputText = File.readTestFile(filepath);
        return CSV.decodeCSV(inputText);
    }

    private static void writeTrades(int i, List<Trade> trades)
        throws FileNotFoundException, UnsupportedEncodingException {
        LOGGER.fine("Writing trades to file");
        final List<String> outputText = CSV.encodeCSV(trades);
        final String filename = "output.test1." + Integer.toString(i) + ".csv";
        final String filePath = Paths.get(absoluteDirectoryOfTestFiles, filename).toString();
        File.writeTestFile(filePath, outputText);
    }
}
