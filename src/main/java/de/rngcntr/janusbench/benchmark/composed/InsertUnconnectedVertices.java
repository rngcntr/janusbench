package de.rngcntr.janusbench.benchmark.composed;

import de.rngcntr.janusbench.benchmark.helper.InsertVerticesBenchmark;
import de.rngcntr.janusbench.util.BenchmarkProperty;
import de.rngcntr.janusbench.util.ComposableBenchmark;
import de.rngcntr.janusbench.util.BenchmarkProperty.Tracking;

public class InsertUnconnectedVertices extends ComposableBenchmark {

    public int runs;
    public int verticesPerRun;

    public InsertUnconnectedVertices() { super(); }

    @Override
    public void buildUp() {
        // create the simple benchmark to be encapsulated by a composed benchmark
        final InsertVerticesBenchmark ivb = new InsertVerticesBenchmark(connection, verticesPerRun);
        ivb.collectBenchmarkProperty(new BenchmarkProperty("vCount", (c) -> g.V().count().next()),
                                     Tracking.BEFORE);

        // run the previously created benchmark multiple times
        addComponent(ivb, runs);
    }
}