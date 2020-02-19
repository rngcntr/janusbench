package de.rngcntr.janusbench.util;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.rngcntr.janusbench.backend.Connection;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

public class ResultLoggerTests {

    @Test
    public void testStreamResults() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ResultLogger.getInstance().setOutputMethod(new PrintStream(baos));
        
        Connection mockedConnection = mock(Connection.class);
        ComposedBenchmark cb = new ComposedBenchmark(mockedConnection);

        BenchmarkResult result0 = new BenchmarkResult(cb);
        BenchmarkResult result1 = new BenchmarkResult(cb);
        result1.injectBenchmarkProperty(new BenchmarkProperty("key", "value"));

        ResultLogger.getInstance().log(result0);
        ResultLogger.getInstance().log(result1);
        
        String output = baos.toString();

        assertTrue(output.contains(result0.toString()), "Output should contain every result");
        assertTrue(output.contains(result1.toString()), "Output should contain every result");
    }

    @Test
    public void testCloseOutputStream() {
        PrintStream ps = mock(PrintStream.class);
        ResultLogger.getInstance().setOutputMethod(ps);
        ResultLogger.getInstance().close();

        verify(ps).close();
    }
}