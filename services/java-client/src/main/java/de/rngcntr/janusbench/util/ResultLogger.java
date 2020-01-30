package de.rngcntr.janusbench.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class ResultLogger {
    // singleton instance
    private static ResultLogger instance;
    private PrintStream outputStream;
    private final String OUTPUT_DIR = "results";
    private final String SEPARATOR = "/";

    private ResultLogger() {
    }

    synchronized public static ResultLogger getInstance() {
        if (instance == null) {
            instance = new ResultLogger();
        }
        return instance;
    }

    synchronized public void log(BenchmarkResult result) {
        outputStream.println(result);
        outputStream.flush();
    }

    synchronized public void setOutputMethod(PrintStream outputStream) {
        this.outputStream = outputStream;
    }

    synchronized public void setOutputMethod(String fileName) throws IOException {
        File outputDirectory = new File(OUTPUT_DIR);
        File outputFile = new File(OUTPUT_DIR + SEPARATOR + fileName);
        outputDirectory.mkdir();
        outputFile.createNewFile();
        outputStream = new PrintStream(new FileOutputStream(OUTPUT_DIR + SEPARATOR + fileName));
    }

    synchronized public void terminate() {
        outputStream.close();
    }
}