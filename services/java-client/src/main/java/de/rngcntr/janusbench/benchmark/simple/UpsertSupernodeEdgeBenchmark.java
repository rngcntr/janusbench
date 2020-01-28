package de.rngcntr.janusbench.benchmark.simple;

import java.util.Random;
import java.util.Date;
import java.util.ArrayList;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.Edge;

import de.rngcntr.janusbench.util.Benchmark;
import de.rngcntr.janusbench.util.BenchmarkResult;

public class UpsertSupernodeEdgeBenchmark extends Benchmark {
    private Vertex supernode;
    private Vertex[] neighbours;

    private Random rand;

    public UpsertSupernodeEdgeBenchmark(GraphTraversalSource g, int stepSize, Vertex supernode) {
        super(g, stepSize);
        this.supernode = supernode;
    }

    public UpsertSupernodeEdgeBenchmark(GraphTraversalSource g, Vertex supernode) {
        super(g);
        this.supernode = supernode;
    }

    @Override
    public void buildUp() {
        neighbours = new Vertex[stepSize];
        
        // get a list of all vertices to select from
        ArrayList<Vertex> allVertices = new ArrayList<Vertex>(g.V().not(__.is(supernode)).toList());
        rand = new Random(System.nanoTime());

        for (int i = 0; i < stepSize; ++i) {
            // randomly choose an incoming vertex
            int selectedIndex = rand.nextInt(allVertices.size());
            neighbours[i] = allVertices.get(selectedIndex);
        }
    }

    @Override
    public void performAction(BenchmarkResult result) {
        for (int index = 0; index < stepSize; ++index) {
            if (g.V(neighbours[index]).in("knows").where(__.is(supernode)).hasNext()) {
                // edge already exists -> update
                Edge e = (Edge) g.V(neighbours[index]).inE("knows").as("e").outV().where(__.is(supernode)).select("e").next();
                e.property("lastSeen", new Date());
            } else {
                // edge does not exist -> insert
                g.addE("knows").from(supernode).to(neighbours[index]).property("lastSeen", new Date()).next();
            }
        }
    }

    @Override
    public void tearDown() {
    }
}
