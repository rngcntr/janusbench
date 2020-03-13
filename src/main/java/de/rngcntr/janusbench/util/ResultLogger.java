package de.rngcntr.janusbench.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * This class handles the logging of {@link BenchmarkResult} objects.
 * 
 * @author Florian Grieskamp
 */
public class ResultLogger implements AutoCloseable {
    
    /**
     * Initialized with "results" by default.
     */
    public final String OUTPUT_DIR = "results";

    private static ResultLogger instance;
    private PrintStream outputStream;
    private final String SEPARATOR = "/";

    private ResultLogger() {}

    /**
     * Returns the singleton instance of this class.
     * 
     * @return An initialized ResultLogger.
     */
    synchronized public static ResultLogger getInstance() {
        if (instance == null) {
            instance = new ResultLogger();
        }
        return instance;
    }

    /**
     * Appends a serialization of result to the output.
     * 
     * @param result The result to write to the output.
     */
    synchronized public void log(final BenchmarkResult result) {
        if (outputStream != null) {
            outputStream.println(result);
            outputStream.flush();
        }
    }

    /**
     * Redirects the output to a custom {@link PrintStream}.
     * 
     * @param outputStream The {@link PrintStream} to use for any further output.
     */
    synchronized public void setOutputMethod(final PrintStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * Redirects the output to a file with a given name.
     * This file will be placed in the directory specified by {@link #OUTPUT_DIR}.
     * 
     * @param fileName The name of the output file.
     * @throws IOException If the given file can't be written.
     */
    synchronized public void setOutputMethod(final String fileName) throws IOException {
        final File outputDirectory = new File(OUTPUT_DIR);
        final File outputFile = new File(OUTPUT_DIR + SEPARATOR + fileName);
        outputDirectory.mkdir();
        outputFile.createNewFile();
        outputStream = new PrintStream(new FileOutputStream(OUTPUT_DIR + SEPARATOR + fileName));
    }

    /**
     * Redirects the output to a given file.
     * If the file or the containing directory does not exist, it's created by this call.
     * 
     * @param file The desired output file.
     * @throws IOException If the given file can't be written.
     */
    synchronized public void setOutputMethod(final File file) throws IOException {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        outputStream = new PrintStream(new FileOutputStream(file));
    }

    /**
     * Closes the underlying {@link PrintStream}.
     */
    synchronized public void close() {
        if (outputStream != null) {
            outputStream.close();
        }
    }
}
