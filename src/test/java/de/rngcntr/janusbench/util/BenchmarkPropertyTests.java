package de.rngcntr.janusbench.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.DefaultGraphTraversal;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class BenchmarkPropertyTests {

    @Test
    public void testGetName() {
        BenchmarkProperty prop = new BenchmarkProperty("key", "value");
        assertEquals("BenchmarkProperty needs to keep key unchanged", "key", prop.getName());
    }

    @Test
    public void testEvaluateString() {
        BenchmarkProperty prop = new BenchmarkProperty("key", "value");
        assertEquals("BenchmarkProperty needs to keep key unchanged", "value", prop.evaluate());
    }

    @Test
    public void testEvaluateTraversal() {
        DefaultGraphTraversal<?,?> mockedStatCollector = mock(DefaultGraphTraversal.class);
        Mockito.doReturn(mockedStatCollector).when(mockedStatCollector).clone();
        Mockito.doReturn("value").when(mockedStatCollector).next();

        BenchmarkProperty prop = new BenchmarkProperty("key", mockedStatCollector);
        assertEquals("BenchmarkProperty needs to evaluate traversals", "value", prop.evaluate());
    }
}
