package de.rngcntr.janusbench.benchmark.simple;

import java.util.Random;
import java.util.concurrent.TimeoutException;
import java.util.Date;
import java.util.ArrayList;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.Edge;

import de.rngcntr.janusbench.tinkerpop.Connection;
import de.rngcntr.janusbench.util.Benchmark;
import de.rngcntr.janusbench.util.BenchmarkResult;

public class UpsertRandomEdgeBenchmark extends Benchmark {
    private Vertex[] a;
    private Vertex[] b;

    private Random rand;

    public UpsertRandomEdgeBenchmark(final Connection connection) {
        super(connection);
    }

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
            int selectedIndexB = rand.nextInt(allVertices.size() - 1);
            if (selectedIndexA == selectedIndexB) {
                // if the same index is selected, use another vertex instead
                selectedIndexB = allVertices.size() - 1;
            }
            b[i] = allVertices.get(selectedIndexB);
        }
    }

    @Override
    public void performAction(final BenchmarkResult result) throws TimeoutException {
        for (int index = 0; index < stepSize; ++index) {
            if (g.V(a[index]).in("knows").where(__.is(b[index])).hasNext()) {
                // edge already exists -> update
                final Edge e = (Edge) g.V(a[index]).inE("knows").as("e").outV().where(__.is(b[index])).select("e")
                        .next();
                e.property("lastSeen", new Date());
            } else {
                // edge does not exist -> insert
                g.addE("knows").from(a[index]).to(b[index]).property("lastSeen", new Date()).next();
            }
        }
    }

    @Override
    public void tearDown() {
    }
}
