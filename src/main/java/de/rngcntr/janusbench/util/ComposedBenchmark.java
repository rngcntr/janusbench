package de.rngcntr.janusbench.util;

import de.rngcntr.janusbench.backend.Connection;

public class ComposedBenchmark extends ComposableBenchmark {

    public ComposedBenchmark(Connection connection) {
        super(connection);
    }


    public ComposedBenchmark(Connection connection, int stepSize) {
        super(connection, stepSize);
    }

}
