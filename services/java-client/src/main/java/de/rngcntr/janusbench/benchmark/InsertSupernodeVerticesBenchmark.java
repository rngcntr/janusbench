package de.rngcntr.janusbench.benchmark;

import java.util.Random;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import de.rngcntr.janusbench.benchmark.*;

public class InsertSupernodeVerticesBenchmark extends AbstractBenchmark {
    private Vertex supernode;

    private String[] names;
    private int nameLength;

    private int[] ages;
    private int minAge;
    private int maxAge;

    private Random rand;

    public InsertSupernodeVerticesBenchmark(GraphTraversalSource g, int stepSize, Vertex supernode) {
        super(g, stepSize);
        this.supernode = supernode;
    }

    public InsertSupernodeVerticesBenchmark(GraphTraversalSource g, Vertex supernode) {
        super(g);
        this.supernode = supernode;
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
            // assume vertex does not exist -> insert
            Vertex insertedVertex = g.addV("person").
                property("name", names[index]).
                property("age", ages[index]).
                next();
            g.addE("knows").
                from(supernode).
                to(insertedVertex).
                property("lastSeen", new Date()).
                property("inVertexID", insertedVertex.id()).
                property("outVertexID", supernode.id()).next();
        }
    }

    public void tearDown() {}

    public String[] getNames() {
        return names;
    }
}
