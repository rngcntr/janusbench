package de.rngcntr.janusbench.util;

import de.rngcntr.janusbench.backend.Connection;
import java.util.ArrayList;

public abstract class ComposableBenchmark extends Benchmark {

    private final ArrayList<Benchmark> components;

    public ComposableBenchmark() {
        super();
        components = new ArrayList<Benchmark>();
	}

    public ComposableBenchmark(final Connection connection) {
        super(connection);
        components = new ArrayList<Benchmark>();
    }

    public ComposableBenchmark(final Connection connection, final int stepSize) {
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
    public void buildUp() {}

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
    public void tearDown() {}
}
