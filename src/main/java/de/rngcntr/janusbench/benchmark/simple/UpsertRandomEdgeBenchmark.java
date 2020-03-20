package de.rngcntr.janusbench.benchmark.simple;

import de.rngcntr.janusbench.backend.Connection;
import de.rngcntr.janusbench.util.Benchmark;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;

/**
 * Selects two random vertices from the graph and connects them with an edge.
 * If the connection already exists, only the edge's property is updated.
 */
public class UpsertRandomEdgeBenchmark extends Benchmark {
    private Vertex[] a;
    private Vertex[] b;

    private Random rand;

    public UpsertRandomEdgeBenchmark(final Connection connection) { super(connection); }

    public UpsertRandomEdgeBenchmark(final Connection connection, final int stepSize) {
        super(connection, stepSize);
    }

    @Override
    public void buildUp() {
        // prepare edges to insert
        a = new Vertex[stepSize];
        b = new Vertex[stepSize];

        // get a list of all vertices to select from
        final ArrayList<Vertex> allVertices = new ArrayList<Vertex>(g.V().toList());
        rand = new Random(System.nanoTime());

        for (int i = 0; i < stepSize; ++i) {
            // randomly choose an incoming vertex
            final int selectedIndexA = rand.nextInt(allVertices.size());
            a[i] = allVertices.get(selectedIndexA);

            // one vertex less to sample from
            int selectedIndexB;
            do {
                selectedIndexB = rand.nextInt(allVertices.size() - 1);
                // if the same index is selected, use another vertex instead
            } while (selectedIndexA == selectedIndexB);
            b[i] = allVertices.get(selectedIndexB);
        }
    }

    @Override
    public void performAction() throws TimeoutException {
        for (int index = 0; index < stepSize; ++index) {
            if (g.V(a[index]).in("knows").where(__.is(b[index])).hasNext()) {
                // edge already exists -> update
                final Edge e = (Edge) g.V(a[index])
                                   .inE("knows")
                                   .as("e")
                                   .outV()
                                   .where(__.is(b[index]))
                                   .select("e")
                                   .next();
                e.property("lastSeen", new Date());
            } else {
                // edge does not exist -> insert
                g.addE("knows").from(a[index]).to(b[index]).property("lastSeen", new Date()).next();
            }
        }
    }

    @Override
    public void tearDown() {}
}
