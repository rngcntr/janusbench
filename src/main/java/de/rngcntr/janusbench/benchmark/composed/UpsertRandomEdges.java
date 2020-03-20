package de.rngcntr.janusbench.benchmark.composed;

import de.rngcntr.janusbench.benchmark.simple.InsertVerticesBenchmark;
import de.rngcntr.janusbench.benchmark.simple.UpsertRandomEdgeBenchmark;
import de.rngcntr.janusbench.util.BenchmarkProperty;
import de.rngcntr.janusbench.util.BenchmarkProperty.Tracking;
import de.rngcntr.janusbench.util.ComposableBenchmark;

public class UpsertRandomEdges extends ComposableBenchmark {

    public int runs;
    public int vertices;
    public int edgesPerRun;

    public UpsertRandomEdges() { super(); }

    public void buildUp() {
        // create the necessary vertices
        final InsertVerticesBenchmark ivb = new InsertVerticesBenchmark(connection, vertices);
        ivb.run();

        final UpsertRandomEdgeBenchmark useb = new UpsertRandomEdgeBenchmark(connection, edgesPerRun);
        BenchmarkProperty existingEdgesProperty = new BenchmarkProperty("existingEdges", () -> connection.g().E().count().next());
        useb.collectBenchmarkProperty(existingEdgesProperty, Tracking.AFTER);

        // run the previously created benchmark multiple times
        addComponent(useb, runs);
    }
}