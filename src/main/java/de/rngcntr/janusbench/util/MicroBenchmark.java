package de.rngcntr.janusbench.util;

import java.util.Map;
import java.util.concurrent.TimeoutException;

import de.rngcntr.janusbench.backend.Connection;

/**
 * A MicroBenchmark is the simplest form of a benchmark, having neihter a buildUp nor a tearDown
 * phase.
 *
 * @author Florian Grieskamp
 */
public class MicroBenchmark extends Benchmark {

    private final String traversalString;
    private final Map<String, Object> parameters;

    public MicroBenchmark(Connection connection, String traversal, Map<String, Object> parameters) {
        super(connection);
        this.traversalString = traversal;
        this.parameters = parameters;
    }

    @Override
    public void buildUp() {}

    @Override
    public void performAction() throws TimeoutException {
        if (parameters != null) {
            connection.submit(traversalString, parameters);
        } else {
            connection.submit(traversalString);
        }
    }

    @Override
    public void tearDown() {}
}