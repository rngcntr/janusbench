package de.rngcntr.janusbench.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import de.rngcntr.janusbench.backend.Connection;
import org.junit.jupiter.api.Test;

public class BenchmarkResultTests {

    @Test
    public void testResultProperties() {
        Connection mockedConn = mock(Connection.class);
        ComposedBenchmark cb = new ComposedBenchmark(mockedConn);
        BenchmarkResult result = new BenchmarkResult(cb, mockedConn);

        assertEquals("action type should be set correctly", cb.getClass().getSimpleName(),
                     (String) result.getBenchmarkProperty("action"));

        BenchmarkProperty prop = new BenchmarkProperty("key", (c) -> "value");
        result.injectBenchmarkProperty(prop);

        assertEquals("action type should be set correctly", "value",
                     (String) result.getBenchmarkProperty("key"));
    }

    @Test
    public void testToString() {
        Connection mockedConn = mock(Connection.class);
        ComposedBenchmark cb = new ComposedBenchmark(mockedConn);
        BenchmarkResult result = new BenchmarkResult(cb, mockedConn);

        BenchmarkProperty prop = new BenchmarkProperty("key", (c) -> "value");
        result.injectBenchmarkProperty(prop);

        assertEquals("String representation needs to output the defined format",
                     "RESULT action=ComposedBenchmark key=value", result.toString());
    }
}
