package de.rngcntr.janusbench.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class ResultLogger implements AutoCloseable {
    // singleton instance
    private static ResultLogger instance;
    private PrintStream outputStream;
    private final String OUTPUT_DIR = "results";
    private final String SEPARATOR = "/";

    private ResultLogger() {}

    synchronized public static ResultLogger getInstance() {
        if (instance == null) {
            instance = new ResultLogger();
        }
        return instance;
    }

    synchronized public void log(final BenchmarkResult result) {
        if (outputStream != null) {
            outputStream.println(result);
            outputStream.flush();
        }
    }

    synchronized public void setOutputMethod(final PrintStream outputStream) {
        this.outputStream = outputStream;
    }

    synchronized public void setOutputMethod(final String fileName) throws IOException {
        final File outputDirectory = new File(OUTPUT_DIR);
        final File outputFile = new File(OUTPUT_DIR + SEPARATOR + fileName);
        outputDirectory.mkdir();
        outputFile.createNewFile();
        outputStream = new PrintStream(new FileOutputStream(OUTPUT_DIR + SEPARATOR + fileName));
    }

    synchronized public void close() {
        if (outputStream != null) {
            outputStream.close();
        }
    }
}
