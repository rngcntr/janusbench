package de.rngcntr.janusbench.util;

import java.util.ArrayList;

import de.rngcntr.janusbench.tinkerpop.Connection;

public class ComposedBenchmark extends Benchmark {

    private final ArrayList<Benchmark> components;

    public ComposedBenchmark(final Connection connection) {
        super(connection);
        components = new ArrayList<Benchmark>();
    }

    public ComposedBenchmark(final Connection connection, final int stepSize) {
        super(connection, stepSize);
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