package de.rngcntr.janusbench.util;

import java.util.HashMap;
import java.util.Map;

/**
 * A BenchmarkResult represents all information gained by a single benchmark run.
 * 
 * @author Florian Grieskamp
 */
public class BenchmarkResult {
    private final HashMap<String, Object> benchmarkProperties;

    /**
     * Initializes a BenchmarkResult using a {@link Benchmark} it belongs to.
     * 
     * @param action The {@link Benchmark} which gathers the results.
     */
    public BenchmarkResult(final Benchmark action) {
        this.benchmarkProperties = new HashMap<String, Object>();
        final BenchmarkProperty actionProperty =
            new BenchmarkProperty("action", action.getClass().getSimpleName());
        injectBenchmarkProperty(actionProperty);
    }

    /**
     * Adds a new {@link BenchmarkProperty} to the BenchmarkResult.
     * 
     * @param property The property to store.
     */
    public void injectBenchmarkProperty(final BenchmarkProperty property) {
        benchmarkProperties.put(property.getName(), property.evaluate());
    }

    /**
     * Returns the object stored in the {@link BenchmarkProperty} with the given name.
     * 
     * @param name The name of the {@link BenchmarkProperty}.
     * @return The value of the {@link BenchmarkProperty}.
     */
    public Object getBenchmarkProperty(final String name) { return benchmarkProperties.get(name); }

    /**
     * Serializes the BenchmarkResult in string representation.
     * 
     * @return The string representation of this BenchmarkResult.
     */
    public String toString() {
        final StringBuilder sb = new StringBuilder("RESULT");
        for (final Map.Entry<String, Object> property : benchmarkProperties.entrySet()) {
            sb.append(" ");
            sb.append(property.getKey());
            sb.append("=");
            sb.append(property.getValue().toString());
        }

        return sb.toString();
    }
}
