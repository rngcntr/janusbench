package de.rngcntr.janusbench.util;

import java.util.HashMap;
import java.util.Map;

public class BenchmarkResult {
    private HashMap<String, Object> benchmarkProperties;

    public BenchmarkResult(Benchmark action) {
        this.benchmarkProperties = new HashMap<String, Object>();
        BenchmarkProperty actionProperty = new BenchmarkProperty("action", action.getClass().getSimpleName());
        injectBenchmarkProperty(actionProperty);
    }

    public void injectBenchmarkProperty(BenchmarkProperty property) {
        benchmarkProperties.put(property.getName(), property.evaluate());
    }

    public Object getBenchmarkProperty(String name) {
        return benchmarkProperties.get(name);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("RESULT");
        for (Map.Entry<String, Object> property : benchmarkProperties.entrySet()) {
            sb.append(" ");
            sb.append(property.getKey());
            sb.append("=");
            sb.append(property.getValue().toString());
        }

        return sb.toString();
    }
}
