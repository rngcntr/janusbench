package de.rngcntr.janusbench.benchmark.simple;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;

import de.rngcntr.janusbench.util.Benchmark;
import de.rngcntr.janusbench.util.BenchmarkResult;

public class InsertVerticesBenchmark extends Benchmark {

    private String[] names;
    private int nameLength;

    private int[] ages;
    private int minAge;
    private int maxAge;

    private Random rand;

    public InsertVerticesBenchmark(GraphTraversalSource g) {
        super(g);
    }

    public InsertVerticesBenchmark(GraphTraversalSource g, int stepSize) {
        super(g, stepSize);
    }

    @Override
    public void buildUp() {
        names = new String[stepSize];
        nameLength = 8;

        ages = new int[stepSize];
        minAge = 18;
        maxAge = 100;

        rand = new Random(System.nanoTime());

        for (int i = 0; i < stepSize; ++i) {
            names[i] = RandomStringUtils.randomAlphanumeric(nameLength);
            ages[i] = rand.nextInt(maxAge - minAge) + minAge;
        }
    }

    @Override
    public void performAction(BenchmarkResult result) {
        for (int index = 0; index < stepSize; ++index) {
            // assume vertex does not exist -> insert
            g.addV("person").
                property("name", names[index]).
                property("age", ages[index]).
                next();
        }
    }

    @Override
    public void tearDown() {}

    public String[] getNames() {
        return names;
    }
}