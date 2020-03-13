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
    private final ValueProvider value;

    /**
     * Initializes a new BenchmarkProperty whose value is produced by a {@link ValueProvider}.
     *
     * @param name The name of the BenchmarkProperty.
     * @param vp The ValueProvider which produces the value of the BenchmarkProperty. It is
     *     evaluated once the Property is serialized.
     */
    public BenchmarkProperty(final String name, final ValueProvider vp) {
        this.name = name;
        this.value = vp;
    }

    /**
     * Initializes a new BenchmarkProperty whose value is pre-determined. If value is a graph
     * traversal, it will be automatically evaluated when needed.
     *
     * @param name The name of the BenchmarkProperty.
     * @param value The pre-determined value.
     */
    public BenchmarkProperty(final String name, final Object value) {
        this(name, () -> {
            if (value instanceof DefaultGraphTraversal<?,?>) {
                final DefaultGraphTraversal<?,?> statCollector = (DefaultGraphTraversal<?,?>) value;
                return statCollector.clone().next();
            } else {
                return value;
            }
        });
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
        return value.evaluate();
    }

    /**
     * A ValueProvider has the ability to return an Object which is only evaluated when needed.
     * Therefore, BenchmarkProperties can contain Objects that are unknown at initialization time
     * but known at execution time.
     */
    @FunctionalInterface
    public interface ValueProvider {
        Object evaluate();
    }
}
