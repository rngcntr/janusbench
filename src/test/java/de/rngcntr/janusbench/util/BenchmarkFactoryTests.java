package de.rngcntr.janusbench.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import de.rngcntr.janusbench.backend.Connection;
import de.rngcntr.janusbench.benchmark.composed.IndexedEdgeExistenceOnSupernode;
import de.rngcntr.janusbench.exceptions.UnavailableBenchmarkException;
import org.junit.jupiter.api.Test;

public class BenchmarkFactoryTests {

    @Test
    public void testGetDefaultBenchmarkCorrectType () {
        Connection mockedConnection = mock(Connection.class);
        Benchmark bm = BenchmarkFactory.getDefaultBenchmark(IndexedEdgeExistenceOnSupernode.class,
                                                            mockedConnection);
        assertTrue(bm instanceof IndexedEdgeExistenceOnSupernode,
                   "BenchmarkFactory has to return the expected type of Benchmark");
    }

    @Test
    public void testGetDefaultBenchmarkNoRegisteredSupplier () {
        Connection mockedConnection = mock(Connection.class);
        assertThrows(
            UnavailableBenchmarkException.class,
            () -> BenchmarkFactory.getDefaultBenchmark(ComposedBenchmark.class, mockedConnection));
    }

    @Test
    public void testConvertCorrectType () {
        Class<? extends Benchmark> benchmarkClass =
            new BenchmarkFactory().convert("IndexedEdgeExistenceOnSupernode");
        assertEquals(benchmarkClass, IndexedEdgeExistenceOnSupernode.class);
    }

    @Test
    public void testConvertNoRegisteredSupplier () {
        assertThrows(
            UnavailableBenchmarkException.class,
            () -> new BenchmarkFactory().convert("ComposedBenchmark"));
    }

    @Test
    public void testGetDefaultBenchmarkIdempotency () {
        Connection mockedConnection = mock(Connection.class);
        Benchmark bm0 = BenchmarkFactory.getDefaultBenchmark(IndexedEdgeExistenceOnSupernode.class,
                                                            mockedConnection);
        Benchmark bm1 = BenchmarkFactory.getDefaultBenchmark(IndexedEdgeExistenceOnSupernode.class,
                                                            mockedConnection);
        assertTrue(bm0 instanceof IndexedEdgeExistenceOnSupernode,
                   "BenchmarkFactory has to return the expected type of Benchmark for the first call");
        assertTrue(bm1 instanceof IndexedEdgeExistenceOnSupernode,
                   "BenchmarkFactory has to return the expected type of Benchmark for the second call");
    }
}