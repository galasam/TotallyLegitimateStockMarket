package test.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class File {

    public static boolean fileExists(String filename) {
        return Files.exists(new java.io.File(filename).toPath());
    }

    public static List<String> readTestFile(String filename) throws IOException {
        return Files.readAllLines(Paths.get(filename));
    }

    public static void writeTestFile(String filename, List<String> contents)
        throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(filename, "UTF-8");
        contents.forEach(writer::println);
        writer.close();
    }

}
