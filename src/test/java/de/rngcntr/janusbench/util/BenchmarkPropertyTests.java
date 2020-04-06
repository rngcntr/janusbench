package de.rngcntr.janusbench.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import de.rngcntr.janusbench.backend.Connection;
import org.junit.jupiter.api.Test;

public class BenchmarkPropertyTests {

    @Test
    public void testGetName() {
        BenchmarkProperty prop = new BenchmarkProperty("key", (c) -> "value");
        assertEquals("BenchmarkProperty needs to keep key unchanged", "key", prop.getName());
    }

    @Test
    public void testEvaluateString() {
        Connection mockedConnection = mock(Connection.class);
        BenchmarkProperty prop = new BenchmarkProperty("key", (c) -> "value");
        assertEquals("BenchmarkProperty needs to keep key unchanged", "value", prop.evaluate(mockedConnection));
    }
}
