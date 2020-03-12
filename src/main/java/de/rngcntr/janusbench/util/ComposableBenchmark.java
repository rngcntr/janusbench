package de.rngcntr.janusbench.util;

import de.rngcntr.janusbench.backend.Connection;
import de.rngcntr.janusbench.backend.configuration.Configuration;

import java.util.ArrayList;

/**
 * Instead of running code directly, a ComposableBenchmark consists of multiple smaller Benchmark
 * units which form a larger unit in combination.
 */
public abstract class ComposableBenchmark extends Benchmark {

    private final ArrayList<Benchmark> components;

    /**
     * Initializes a ComposableBenchmark with a step size of 1.
     * This ComposableBenchmark does not have connection and therefore is unable to run until {@link
     * #setConnection(Connection)} is called.
     */
    public ComposableBenchmark() {
        super();
        components = new ArrayList<Benchmark>();
    }

    /**
     * Initializes a ComposableBenchmark using only a connection. The step size will be set to 1.
     *
     * @param connection The connection to the graph backend.
     */
    public ComposableBenchmark(final Connection connection) {
        super(connection);
        components = new ArrayList<Benchmark>();
    }

    /**
     * Initializes a ComposableBenchmark using a connection and step size.
     * 
     * @param connection The connection to the graph backend.
     * @param stepSize The size of a single benchmark run.
     */
    public ComposableBenchmark(final Connection connection, final int stepSize) {
        super(connection, stepSize);
        components = new ArrayList<Benchmark>();
    }

    /**
     * Adds a single component to the ComposableBenchmark.
     * All components will be executed in the same order they were added.
     * 
     * @param component The benchmark component to add.
     */
    public void addComponent(final Benchmark component) {
        if (component != null && component != this) {
            component.setConfiguration(configuration);
            components.add(component);
        }
    }

    /**
     * Sets the configuration which is used to run this benchmark. This configuration is later used
     * to log the used storage and index backends.
     * 
     * @param configuration The configuration from which the used backends are obtained.
     */
    public void setConfiguration(final Configuration configuration) {
        this.configuration = configuration;
        for (final Benchmark component : components) {
            component.setConfiguration(configuration);
        }
    }

    /**
     * Adds a single component to the ComposableBenchmark.
     * All components will be executed in the same order they were added.
     * 
     * @param component The benchmark component to add.
     * @param repetitions The number of times this component will be executed.
     */
    public void addComponent(final Benchmark component, final int repetitions) {
        if (component != null && component != this) {
            for (int repCnt = 0; repCnt < repetitions; ++repCnt) {
                component.setConfiguration(configuration);
                components.add(component);
            }
        }
    }

    /**
     * @see Benchmark#buildUp()
     */
    @Override
    public void buildUp() {}

    /**
     * Executes all components in the same order they were addded.
     * Each component is executed according to the number of repetitions.
     */
    @Override
    public void performAction() {
        for (int step = 0; step < stepSize; ++step) {
            for (final Benchmark component : components) {
                component.run();
                results.addAll(component.getResults());
                component.resetResults();
            }
        }
    }

    /**
     * @see Benchmark#tearDown()
     */
    @Override
    public void tearDown() {}
}
