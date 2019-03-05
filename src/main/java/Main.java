
import DataObjects.Order;
import DataObjects.Trade;
import Utils.CSV;
import Utils.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    final private static Logger LOGGER = Logger.getLogger("MARKET_LOGGER");

    private static final int phaseNumber = 4;
    private static final String relativeDirectoryOfTestFiles = "test truths/Phase" + Integer.toString(phaseNumber);
    private static final String absoluteDirectoryOfTestFiles = Paths
        .get(System.getProperty("user.dir"), relativeDirectoryOfTestFiles).toString();

    private static void setupLogger() {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        LOGGER.addHandler(consoleHandler);
        LOGGER.setLevel(Level.ALL);
    }

    public static void main(String[] args) throws IOException {
        setupLogger();
        LOGGER.info("Running All Tests in: " + relativeDirectoryOfTestFiles);

        int i = 1;
        while(testFileExists(i)) {
            runTest(i++);
        }

        //IntStream.range(1,12).forEach(Main::runTest);
        //runTest(8);
    }

    private static boolean testFileExists(int testNumber) {
        String filepath = getInputFilePath(testNumber);
        return Utils.File.fileExists(filepath);
    }

    private static void runTest(int i) throws IOException {
        LOGGER.fine(String.format("Running test %d", i));
        final List<Order> orders = readOrders(i);
        final List<Trade> trades = MarketManager.getResultingTrades(orders);
        writeTrades(i, trades);
    }

    private static List<Order> readOrders(int testNumber) throws IOException {
        LOGGER.fine("Reading Orders from file");
        String filepath = getInputFilePath(testNumber);
        final List<String> inputText = File.readTestFile(filepath);
        return CSV.decodeCSV(inputText);
    }

    private static String getInputFilePath(int testNumber) {
        final String filename = String.format("input.test%d.%d.csv", phaseNumber, testNumber);
        return Paths.get(absoluteDirectoryOfTestFiles, filename).toString();
    }

    private static void writeTrades(int testNumber, List<Trade> trades)
        throws FileNotFoundException, UnsupportedEncodingException {
        LOGGER.fine("Writing trades to file");
        final List<String> outputText = CSV.encodeCSV(trades);
        final String filename = String.format("output.test%d.%d.csv", phaseNumber, testNumber);
        final String filePath = Paths.get(absoluteDirectoryOfTestFiles, filename).toString();
        File.writeTestFile(filePath, outputText);
    }
}
