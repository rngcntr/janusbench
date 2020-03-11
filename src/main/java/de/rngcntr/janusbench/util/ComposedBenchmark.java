package de.rngcntr.janusbench.util;

import de.rngcntr.janusbench.backend.Connection;

/**
 * Instead of running code directly, a ComposedBenchmark consists of multiple smaller Benchmark
 * units which form a larger unit in combination.
 */
public class ComposedBenchmark extends ComposableBenchmark {

    /**
     * Initializes a ComposedBenchmark using only a connection. The step size will be set to 1.
     *
     * @param connection The connection to the graph backend.
     */
    public ComposedBenchmark(Connection connection) { super(connection); }

    /**
     * Initializes a ComposedBenchmark using a connection and step size.
     * 
     * @param connection The connection to the graph backend.
     * @param stepSize The size of a single benchmark run.
     */
    public ComposedBenchmark(Connection connection, int stepSize) { super(connection, stepSize); }
}
