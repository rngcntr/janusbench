package de.rngcntr.janusbench.util;

import java.util.ArrayList;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;

public class ComposedBenchmark extends Benchmark {

    private final ArrayList<Benchmark> components;

    public ComposedBenchmark(final GraphTraversalSource g) {
        super(g);
        components = new ArrayList<Benchmark>();
    }

    public ComposedBenchmark(final GraphTraversalSource g, final int stepSize) {
        super(g, stepSize);
        components = new ArrayList<Benchmark>();
    }

    public void addComponent(final Benchmark component) {
        if (component != null && component != this) {
            components.add(component);
        }
    }

    public void addComponent(final Benchmark component, final int repetitions) {
        if (component != null && component != this) {
            for (int repCnt = 0; repCnt < repetitions; ++repCnt) {
                components.add(component);
            }
        }
    }

    @Override
    public void buildUp() {
    }

    @Override
    public void performAction(final BenchmarkResult result) {
        for (int step = 0; step < stepSize; ++step) {
            for (final Benchmark component : components) {
                component.run();
                results.addAll(component.getResults());
                component.resetResults();
            }
        }
    }

    @Override
    public void tearDown() {
    }
}