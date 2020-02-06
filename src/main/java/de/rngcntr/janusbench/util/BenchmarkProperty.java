package de.rngcntr.janusbench.util;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.DefaultGraphTraversal;

public class BenchmarkProperty {

    public enum Tracking {
        BEFORE,
        AFTER;
    }

    public static final Tracking BEFORE = Tracking.BEFORE;
    public static final Tracking AFTER = Tracking.AFTER;

    private final String name;
    private final Object value;

    public BenchmarkProperty(final String name, final DefaultGraphTraversal<?, ?> statCollector) {
        this.name = name;
        this.value = statCollector;
    }

    public BenchmarkProperty(final String name, final Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() { return name; }

    public Object evaluate() {
        if (value instanceof DefaultGraphTraversal<?, ?>) {
            final DefaultGraphTraversal<?, ?> statCollector = (DefaultGraphTraversal<?, ?>)value;
            return statCollector.clone().next();
        } else {
            return value;
        }
    }
}
