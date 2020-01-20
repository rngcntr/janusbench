package de.rngcntr.janusbench.benchmark;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import de.rngcntr.janusbench.benchmark.*;

public class InsertVerticesBenchmark extends AbstractBenchmark {
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

    public void performAction(BenchmarkResult result) {
        for (int index = 0; index < stepSize; ++index) {
            g.addV("person").
                property("name", names[index]).
                property("age", ages[index]).
                next();
        }
    }

    public void tearDown() {}
}
