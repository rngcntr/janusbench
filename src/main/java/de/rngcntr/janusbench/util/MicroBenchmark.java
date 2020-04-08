package de.rngcntr.janusbench.util;

import de.rngcntr.janusbench.backend.Connection;
import de.rngcntr.janusbench.util.BenchmarkProperty.Tracking;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeoutException;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.driver.ResultSet;

/**
 * A MicroBenchmark is the simplest form of a benchmark, having neihter a buildUp nor a tearDown
 * phase.
 *
 * @author Florian Grieskamp
 */
public class MicroBenchmark extends Benchmark {

    private final String traversalString;
    private final Map<String, Object> parameters;

    private final List<BuildUpQuery> buildUpQueries;

    public MicroBenchmark(Connection connection, String traversal, Map<String, Object> parameters) {
        super(connection);
        this.traversalString = traversal;

        this.parameters = new HashMap<String, Object>();
        if (parameters != null) {
            this.parameters.putAll(parameters);
        }

        this.buildUpQueries = new ArrayList<BuildUpQuery>();
    }

    public void setBuildUp(List<BuildUpQuery> buildUp) {
        if (buildUp != null) {
            this.buildUpQueries.addAll(buildUp);
        }
    }

    @Override
    public void buildUp() {
        for (BuildUpQuery query : buildUpQueries) {
            String key = query.getName();
            Object value = query.evaluate(connection, parameters);
            parameters.put(key, value);
        }
    }

    @Override
    public void performAction() throws TimeoutException {
        ResultSet resultSet = connection.submit(traversalString, parameters);
        Iterator<Result> resultIterator = resultSet.iterator();
        try {
            Object result = resultIterator.next().getObject();
            BenchmarkProperty resultProperty =
                new BenchmarkProperty("result", (c) -> result);
            collectBenchmarkProperty(resultProperty, Tracking.AFTER);
        } catch (NoSuchElementException | CompletionException ignored) {
            BenchmarkProperty resultProperty =
                new BenchmarkProperty("result", (c) -> "null");
            collectBenchmarkProperty(resultProperty, Tracking.AFTER);
        }
    }

    @Override
    public void tearDown() {}
}