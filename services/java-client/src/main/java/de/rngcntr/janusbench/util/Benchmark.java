package de.rngcntr.janusbench.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;

import de.rngcntr.janusbench.tinkerpop.Connection;
import de.rngcntr.janusbench.util.BenchmarkProperty;

public abstract class Benchmark implements Runnable {
    protected ArrayList<BenchmarkResult> results;
    protected Connection connection;
    protected GraphTraversalSource g;

    protected int stepSize;

    private ArrayList<BenchmarkProperty> trackBeforeRun;
    private ArrayList<BenchmarkProperty> trackAfterRun;

    public Benchmark(Connection connection) {
        this.connection = connection;
        this.g = connection.g();
        this.stepSize = 1;
        this.results = new ArrayList<BenchmarkResult>();
        this.trackBeforeRun = new ArrayList<BenchmarkProperty>();
        this.trackAfterRun = new ArrayList<BenchmarkProperty>();
    }

    public Benchmark(Connection connection, int stepSize) {
        this(connection);
        this.stepSize = stepSize;
    }

    public void run() {
        BenchmarkResult result = new BenchmarkResult(this);
        buildUp();

        BenchmarkProperty stepSizeProperty = new BenchmarkProperty("stepSize", stepSize);
        result.injectBenchmarkProperty(stepSizeProperty);

        for (BenchmarkProperty beforeProperty : trackBeforeRun) {
            result.injectBenchmarkProperty(beforeProperty);
        }

        long startTime = System.nanoTime();
        performAction(result);

        boolean committed = false;
        while (!committed) {
            try {
                // commits are automatically managed
                connection.submit("g.tx().commit()");
                committed = true;
            } catch (Exception ex) {}
        }

        long stopTime = System.nanoTime();

        for (BenchmarkProperty afterProperty : trackAfterRun) {
            result.injectBenchmarkProperty(afterProperty);
        }

        BenchmarkProperty timeProperty = new BenchmarkProperty("time", (stopTime - startTime) / 1000000.0 / stepSize);
        result.injectBenchmarkProperty(timeProperty);

        tearDown();

        results.add(result);
        System.out.println(result); // DEBUG
    }

    public void runUntil(IBreakCondition breakCondition) {
        // check break condition on latest result
        do {
            run();
        } while (!breakCondition.stop(results.get(results.size() - 1)));
    }

    public abstract void buildUp();
    public abstract void performAction(BenchmarkResult result);
    public abstract void tearDown();

    public ArrayList<BenchmarkResult> getResults() {
        return results;
    }

    public List<BenchmarkResult> getResults(Class<?> cls) {
        return results.stream().filter(
                r -> r.getBenchmarkProperty("action").equals(cls.getSimpleName())
            ).collect(Collectors.toList());
    }

    public void resetResults() {
        results.clear();
    }

    public void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }

    public void collectBenchmarkProperty (BenchmarkProperty property, BenchmarkProperty.Tracking tracking) {
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
        boolean stop (BenchmarkResult result);
    }
}