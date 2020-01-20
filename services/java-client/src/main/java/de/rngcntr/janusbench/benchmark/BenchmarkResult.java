package de.rngcntr.janusbench.benchmark;

import java.util.Map;
import java.util.HashMap;

public class BenchmarkResult {
    private HashMap<String, Object> benchmarkProperties;

    public BenchmarkResult(AbstractBenchmark action) {
        this.benchmarkProperties = new HashMap<String, Object>();
        injectBenchmarkProperty("action", action.getClass().getName());
    }

    public void injectBenchmarkProperty (String name, Object value) {
        benchmarkProperties.put(name, value);
    }

    public Object getBenchmarkProperty (String name) {
        return benchmarkProperties.get(name);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("RESULT");
        for (Map.Entry property : benchmarkProperties.entrySet()) {
            sb.append(" ");
            sb.append(property.getKey().toString());
            sb.append("=");
            sb.append(property.getValue().toString());
        }

        return sb.toString();
    }
}
