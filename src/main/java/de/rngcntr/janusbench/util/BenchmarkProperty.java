package de.rngcntr.janusbench.util;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.DefaultGraphTraversal;

/**
 * Represents a key information of a benchmark run.
 * This can either be a parameter or a measured result.
 * 
 * @author Florian Grieskamp
 */
public class BenchmarkProperty {

    /**
     * Tracking allows BenchmarkProperties to be gathered before or after a benchmark run.
     */
    public enum Tracking {
        BEFORE,
        AFTER;
    }

    /**
     * The property is collected before the benchmark run is started.
     */
    public static final Tracking BEFORE = Tracking.BEFORE;

    /**
     * The property is collected after the benchmark run has finished.
     */
    public static final Tracking AFTER = Tracking.AFTER;

    private final String name;
    private final Object value;

    /**
     * Initializes a new BenchmarkProperty whose value is determined by the result of a traversal.
     * 
     * @param name The name of the BenchmarkProperty.
     * @param statCollector The Traversal which collects the result value.
     */
    public BenchmarkProperty(final String name, final DefaultGraphTraversal<?, ?> statCollector) {
        this.name = name;
        this.value = statCollector;
    }

    /**
     * Initializes a new BenchmarkProperty whose value is pre-determined.
     * 
     * @param name The name of the BenchmarkProperty.
     * @param value The pre-determined value.
     */
    public BenchmarkProperty(final String name, final Object value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Returns the name of the BenchmarkProperty.
     * 
     * @return The name of the BenchmarkProperty.
     */
    public String getName() { return name; }

    /**
     * In case the stored value is a traversal, this traversal is run and it's result is returned.
     * Else, the pre-determined value is returned immediately.
     * 
     * @return The evaluated result of the BenchmarkProperty.
     */
    public Object evaluate() {
        if (value instanceof DefaultGraphTraversal<?, ?>) {
            final DefaultGraphTraversal<?, ?> statCollector = (DefaultGraphTraversal<?, ?>) value;
            return statCollector.clone().next();
        } else {
            return value;
        }
    }
}
