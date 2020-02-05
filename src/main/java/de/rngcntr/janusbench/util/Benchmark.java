package de.rngcntr.janusbench.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;

import de.rngcntr.janusbench.tinkerpop.Connection;
import de.rngcntr.janusbench.util.BenchmarkProperty;

public abstract class Benchmark implements Runnable {
    protected ArrayList<BenchmarkResult> results;
    protected Connection connection;
    protected GraphTraversalSource g;

    protected int stepSize;

    private final ArrayList<BenchmarkProperty> trackBeforeRun;
    private final ArrayList<BenchmarkProperty> trackAfterRun;

    private boolean collectResults;

    public Benchmark(final Connection connection) {
        this.connection = connection;
        this.g = connection.g();
        this.stepSize = 1;
        this.results = new ArrayList<BenchmarkResult>();
        this.trackBeforeRun = new ArrayList<BenchmarkProperty>();
        this.trackAfterRun = new ArrayList<BenchmarkProperty>();
        this.collectResults = true;
    }

    public Benchmark(final Connection connection, final int stepSize) {
        this(connection);
        this.stepSize = stepSize;
    }

    public void setCollectResults(final boolean collectResults) {
        this.collectResults = collectResults;
    }

    public void run() {
        buildUp();

        final BenchmarkResult result = new BenchmarkResult(this);
        final BenchmarkProperty stepSizeProperty = new BenchmarkProperty("stepSize", stepSize);
        result.injectBenchmarkProperty(stepSizeProperty);

        for (final BenchmarkProperty beforeProperty : trackBeforeRun) {
            result.injectBenchmarkProperty(beforeProperty);
        }

        final long startTime = System.nanoTime();

        boolean successful = false;

        while (!successful) {
            try {
                performAction(result);
                connection.submit("g.tx().commit()");
                successful = true;
            } catch (TimeoutException tex) {
                connection.submit("g.tx().rollback()");
            }
        }

        final long stopTime = System.nanoTime();

        for (final BenchmarkProperty afterProperty : trackAfterRun) {
            result.injectBenchmarkProperty(afterProperty);
        }

        final BenchmarkProperty timeProperty = new BenchmarkProperty("time",
                (stopTime - startTime) / 1000000.0);
        result.injectBenchmarkProperty(timeProperty);

        tearDown();

        if (collectResults) {
            results.add(result);
            ResultLogger.getInstance().log(result);
        }
    }

    public void runUntil(final IBreakCondition breakCondition) {
        // check break condition on latest result
        do {
            run();
        } while (!breakCondition.stop(results.get(results.size() - 1)));
    }

    public abstract void buildUp();

    public abstract void performAction(BenchmarkResult result) throws TimeoutException;

    public abstract void tearDown();

    public ArrayList<BenchmarkResult> getResults() {
        return results;
    }

    public List<BenchmarkResult> getResults(final Class<?> cls) {
        return results.stream().filter(r -> r.getBenchmarkProperty("action").equals(cls.getSimpleName()))
                .collect(Collectors.toList());
    }

    public void resetResults() {
        results.clear();
    }

    public void setStepSize(final int stepSize) {
        this.stepSize = stepSize;
    }

    public void collectBenchmarkProperty(final BenchmarkProperty property, final BenchmarkProperty.Tracking tracking) {
        switch (tracking) {
        case BEFORE:
            trackBeforeRun.add(property);
            break;
        case AFTER:
            trackAfterRun.add(property);
            break;
        }
    }

    public interface IBreakCondition {
        boolean stop(BenchmarkResult result);
    }
}
