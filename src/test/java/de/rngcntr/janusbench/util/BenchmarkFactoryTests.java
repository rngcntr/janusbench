package de.rngcntr.janusbench.util;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import de.rngcntr.janusbench.backend.Connection;
import de.rngcntr.janusbench.benchmark.composed.IndexedEdgeExistenceOnSupernode;
import org.junit.jupiter.api.Test;

public class BenchmarkFactoryTests {

    @Test
    public void testCorrectType () {
        Connection mockedConnection = mock(Connection.class);
        Benchmark bm = BenchmarkFactory.getDefaultBenchmark(IndexedEdgeExistenceOnSupernode.class,
                                                            mockedConnection);
        assertTrue(bm instanceof IndexedEdgeExistenceOnSupernode,
                   "BenchmarkFactory has to return the expected type of Benchmark");
    }

    @Test
    public void testIdempotency () {
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