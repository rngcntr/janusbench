package de.rngcntr.janusbench.benchmark.simple;

import de.rngcntr.janusbench.backend.Connection;
import de.rngcntr.janusbench.util.Benchmark;
import de.rngcntr.janusbench.util.BenchmarkResult;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public class UpsertSupernodeEdgeBenchmark extends Benchmark {
    private final Vertex supernode;
    private Vertex[] neighbours;

    private Random rand;

    public UpsertSupernodeEdgeBenchmark(final Connection connection, final int stepSize,
                                        final Vertex supernode) {
        super(connection, stepSize);
        this.supernode = supernode;
    }

    public UpsertSupernodeEdgeBenchmark(final Connection connection, final Vertex supernode) {
        super(connection);
        this.supernode = supernode;
    }

    @Override
    public void buildUp() {
        neighbours = new Vertex[stepSize];

        // get a list of all vertices to select from
        final ArrayList<Vertex> allVertices =
            new ArrayList<Vertex>(g.V().not(__.is(supernode)).toList());
        rand = new Random(System.nanoTime());

        for (int i = 0; i < stepSize; ++i) {
            // randomly choose an incoming vertex
            final int selectedIndex = rand.nextInt(allVertices.size());
            neighbours[i] = allVertices.get(selectedIndex);
        }
    }

    @Override
    public void performAction(final BenchmarkResult result) throws TimeoutException {
        for (int index = 0; index < stepSize; ++index) {
            if (g.V(neighbours[index]).in("knows").where(__.is(supernode)).hasNext()) {
                // edge already exists -> update
                final Edge e = (Edge)g.V(neighbours[index])
                                   .inE("knows")
                                   .as("e")
                                   .outV()
                                   .where(__.is(supernode))
                                   .select("e")
                                   .next();
                e.property("lastSeen", new Date());
            } else {
                // edge does not exist -> insert
                g.addE("knows")
                    .from(supernode)
                    .to(neighbours[index])
                    .property("lastSeen", new Date())
                    .next();
            }
        }
    }

    @Override
    public void tearDown() {}
}
