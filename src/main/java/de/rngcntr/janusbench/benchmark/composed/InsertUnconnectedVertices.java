package de.rngcntr.janusbench.benchmark.composed;

import de.rngcntr.janusbench.benchmark.simple.InsertVerticesBenchmark;
import de.rngcntr.janusbench.util.ComposableBenchmark;

public class InsertUnconnectedVertices extends ComposableBenchmark {

    public int runs;
    public int verticesPerRun;

    public InsertUnconnectedVertices() { super(); }

    @Override
    public void buildUp() {
        // create the simple benchmark to be encapsulated by a composed benchmark
        final InsertVerticesBenchmark ivb = new InsertVerticesBenchmark(connection, verticesPerRun);

        // run the previously created benchmark multiple times
        addComponent(ivb, runs);
    }
}