package de.rngcntr.janusbench;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import de.rngcntr.janusbench.tinkerpop.Connection;
import de.rngcntr.janusbench.util.BenchmarkProperty;
import de.rngcntr.janusbench.util.BenchmarkResult;
import de.rngcntr.janusbench.util.ComposedBenchmark;

@Testcontainers
public class ResultTests {


    @Test
    public void testResultProperties() {
        Connection conn = new Connection(null);
        ComposedBenchmark cb = new ComposedBenchmark(conn);
        BenchmarkResult result = new BenchmarkResult(cb);

        assertEquals("action type should be set correctly", (String) result.getBenchmarkProperty("action"), cb.getClass().getSimpleName());

        BenchmarkProperty prop = new BenchmarkProperty("key", "value");
        result.injectBenchmarkProperty(prop);

        assertEquals("action type should be set correctly", (String) result.getBenchmarkProperty("key"), "value");
    }
}