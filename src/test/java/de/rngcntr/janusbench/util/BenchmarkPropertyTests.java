package de.rngcntr.janusbench.util;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

public class BenchmarkPropertyTests {

    @Test
    public void testGetName() {
        BenchmarkProperty prop = new BenchmarkProperty("key", () -> "value");
        assertEquals("BenchmarkProperty needs to keep key unchanged", "key", prop.getName());
    }

    @Test
    public void testEvaluateString() {
        BenchmarkProperty prop = new BenchmarkProperty("key", () -> "value");
        assertEquals("BenchmarkProperty needs to keep key unchanged", "value", prop.evaluate());
    }
}
