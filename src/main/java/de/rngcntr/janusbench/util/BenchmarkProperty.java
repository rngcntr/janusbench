package de.rngcntr.janusbench.util;

import de.rngcntr.janusbench.backend.Connection;

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

    public BenchmarkProperty(final String name, final String query) {
        this.name = name;
        this.value = (c) -> c.submit(query).all().join().get(0).getObject();
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
    public Object evaluate(Connection connection) {
        return value.evaluate(connection);
    }

    /**
     * A ValueProvider has the ability to return an Object which is only evaluated when needed.
     * Therefore, BenchmarkProperties can contain Objects that are unknown at initialization time
     * but known at execution time.
     */
    @FunctionalInterface
    public interface ValueProvider {
        Object evaluate(Connection connection);
    }
}
