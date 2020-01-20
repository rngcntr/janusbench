package de.rngcntr.janusbench.benchmark;

import java.util.ArrayList;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import de.rngcntr.janusbench.benchmark.*;

public abstract class AbstractBenchmark implements Runnable {
    protected ArrayList<BenchmarkResult> results;
    protected GraphTraversalSource g;

    protected int stepSize;
    protected int numThreads;

    private boolean collectStats;

    public AbstractBenchmark(GraphTraversalSource g) {
        this.results = new ArrayList<BenchmarkResult>();
        this.g = g;
        this.stepSize = 1;
        this.numThreads = 1;
        this.collectStats = true;
    }

    public AbstractBenchmark(GraphTraversalSource g, int stepSize) {
        this(g);
        this.stepSize = stepSize;
    }

    public void run() {
        BenchmarkResult result = new BenchmarkResult(this);
        buildUp();

        if (collectStats) {
            result.injectBenchmarkProperty("vBefore", g.V().count().next());
            result.injectBenchmarkProperty("eBefore", g.E().count().next());
        }

        result.injectBenchmarkProperty("stepSize", stepSize);

        long startTime = System.nanoTime();
        performAction(result);
        long stopTime = System.nanoTime();

        result.injectBenchmarkProperty("time", (stopTime - startTime) * 0.000001 / stepSize);

        if (collectStats) {
            result.injectBenchmarkProperty("vAfter", g.V().count().next());
            result.injectBenchmarkProperty("eAfter", g.E().count().next());
        }

        tearDown();

        results.add(result);
    }

    public void runUntil(IBreakCondition breakCondition) {
        // check break condition on latest result
        do {
            run();
        } while (breakCondition.stop(results.get(results.size() - 1)) == false);
    }

    public abstract void buildUp();
    public abstract void performAction(BenchmarkResult result);
    public abstract void tearDown();

    public ArrayList<BenchmarkResult> getResults() {
        return results;
    }

    public void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }

    public void setCollectStats(boolean collectStats) {
        this.collectStats = collectStats;
    }

    public interface IBreakCondition {
        boolean stop (BenchmarkResult result);
    }
}
