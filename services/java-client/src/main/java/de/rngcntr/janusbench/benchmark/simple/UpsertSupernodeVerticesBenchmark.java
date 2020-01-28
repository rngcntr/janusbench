package de.rngcntr.janusbench.benchmark.simple;

import java.util.Random;
import java.util.Date;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.Edge;

import org.apache.commons.lang3.RandomStringUtils;

import de.rngcntr.janusbench.util.Benchmark;
import de.rngcntr.janusbench.util.BenchmarkResult;

public class UpsertSupernodeVerticesBenchmark extends Benchmark {
    private Vertex supernode;

    private String[] names;
    private int nameLength;

    private int[] ages;
    private int minAge;
    private int maxAge;

    private Random rand;

    public UpsertSupernodeVerticesBenchmark(GraphTraversalSource g, int stepSize, Vertex supernode) {
        super(g, stepSize);
        this.supernode = supernode;
    }

    public UpsertSupernodeVerticesBenchmark(GraphTraversalSource g, Vertex supernode) {
        super(g);
        this.supernode = supernode;
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
            if (g.V().has("name", names[index]).in("knows").where(__.is(supernode)).hasNext()) {
                // vertex already exists -> update edge
                Edge e = (Edge) g.V().has("name", names[index]).inE("knows").as("e").outV().where(__.is(supernode)).select("e").next();
                e.property("lastSeen", new Date());
            } else {
                // vertex does not exist -> insert
                Vertex insertedVertex = g.addV("person").
                    property("name", names[index]).
                    property("age", ages[index]).
                    next();
                g.addE("knows").from(supernode).to(insertedVertex).property("lastSeen", new Date()).next();
            }
        }
    }

    @Override
    public void tearDown() {}
}
