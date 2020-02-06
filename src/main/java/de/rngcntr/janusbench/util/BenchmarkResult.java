package de.rngcntr.janusbench.util;

import java.util.HashMap;
import java.util.Map;

public class BenchmarkResult {
    private final HashMap<String, Object> benchmarkProperties;

    public BenchmarkResult(final Benchmark action) {
        this.benchmarkProperties = new HashMap<String, Object>();
        final BenchmarkProperty actionProperty =
            new BenchmarkProperty("action", action.getClass().getSimpleName());
        injectBenchmarkProperty(actionProperty);
    }

    public void injectBenchmarkProperty(final BenchmarkProperty property) {
        benchmarkProperties.put(property.getName(), property.evaluate());
    }

    public Object getBenchmarkProperty(final String name) { return benchmarkProperties.get(name); }

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
