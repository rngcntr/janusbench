package de.rngcntr.janusbench.benchmark.simple;

import de.rngcntr.janusbench.backend.Connection;
import de.rngcntr.janusbench.util.Benchmark;
import de.rngcntr.janusbench.util.BenchmarkResult;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public class UpsertSupernodeVerticesBenchmark extends Benchmark {
    private final Vertex supernode;

    private String[] names;
    private int nameLength;

    private int[] ages;
    private int minAge;
    private int maxAge;

    private Random rand;

    public UpsertSupernodeVerticesBenchmark(final Connection connection, final int stepSize,
                                            final Vertex supernode) {
        super(connection, stepSize);
        this.supernode = supernode;
    }

    public UpsertSupernodeVerticesBenchmark(final Connection connection, final Vertex supernode) {
        super(connection);
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
    public void performAction(final BenchmarkResult result) throws TimeoutException {
        for (int index = 0; index < stepSize; ++index) {
            if (g.V().has("name", names[index]).in("knows").where(__.is(supernode)).hasNext()) {
                // vertex already exists -> update edge
                final Edge e = (Edge) g.V()
                                   .has("name", names[index])
                                   .inE("knows")
                                   .as("e")
                                   .outV()
                                   .where(__.is(supernode))
                                   .select("e")
                                   .next();
                e.property("lastSeen", new Date());
            } else {
                // vertex does not exist -> insert
                final Vertex insertedVertex = g.addV("person")
                                                  .property("name", names[index])
                                                  .property("age", ages[index])
                                                  .next();
                g.addE("knows")
                    .from(supernode)
                    .to(insertedVertex)
                    .property("lastSeen", new Date())
                    .next();
            }
        }
    }

    @Override
    public void tearDown() {}
}
