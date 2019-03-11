import Utils.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class FileOperationTests {

    private static final String relativeTestResourcesDirPath = "src/test/resources";
    private static final String absoluteTestResourcesDirPath = Paths
        .get(System.getProperty("user.dir"), relativeTestResourcesDirPath).toString();

    private static final String fileContents = "example";

@Test
public void canDetectFileExists() {
    final String pathToFile = Paths.get(absoluteTestResourcesDirPath, "file").toString();
    Assert.assertTrue("Should be able to detect that a file exists", File.fileExists(pathToFile));
}

@Test
public void canDetectFileDoesNotExist() {
    final String pathToFile = Paths.get(absoluteTestResourcesDirPath, "notThere").toString();
    Assert.assertTrue("Should be able to detect that a file does not exist", !File.fileExists(pathToFile));
}

@Test
public void canReadFromFile() throws IOException {
    final String pathToFile = Paths.get(absoluteTestResourcesDirPath, "file").toString();
    List<String> lines = File.readTestFile(pathToFile);
    Assert.assertTrue("Should be able to read from file", lines.size() == 1 && lines.get(0).equals(fileContents));
}

@Test
public void canWriteToFile() throws IOException {
    final String pathToFile = Paths.get(absoluteTestResourcesDirPath, "output").toString();
    List<String> lines = new LinkedList<>();
    lines.add("test");
    File.writeTestFile(pathToFile, lines);
    List<String> linesTest = Files.readAllLines(Paths.get(pathToFile));
    Assert.assertTrue("Should be able to write to file", lines.equals(linesTest));
    Files.delete(Paths.get(pathToFile));
}

}
