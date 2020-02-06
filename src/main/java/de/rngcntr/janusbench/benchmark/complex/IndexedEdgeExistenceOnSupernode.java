package de.rngcntr.janusbench.benchmark.complex;

import de.rngcntr.janusbench.benchmark.simple.EdgeExistenceBenchmark;
import de.rngcntr.janusbench.benchmark.simple.InsertSupernodeVerticesBenchmark;
import de.rngcntr.janusbench.benchmark.simple.InsertVerticesBenchmark;
import de.rngcntr.janusbench.tinkerpop.Connection;
import de.rngcntr.janusbench.util.BenchmarkProperty;
import de.rngcntr.janusbench.util.BenchmarkProperty.Tracking;
import de.rngcntr.janusbench.util.ComposedBenchmark;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public class IndexedEdgeExistenceOnSupernode extends ComposedBenchmark {

    public IndexedEdgeExistenceOnSupernode(final Connection connection, final int runs,
                                           final int stepsPerRun, final int edgesPerStep) {
        super(connection);

        // prepare a vertex to later become a supernode
        final InsertVerticesBenchmark ivb = new InsertVerticesBenchmark(connection, 1);
        ivb.run();
        final Vertex supernode = connection.g().V().next();

        // create the inserter benchmark
        final InsertSupernodeVerticesBenchmark isvb =
            new InsertSupernodeVerticesBenchmark(connection, edgesPerStep, supernode);
        final BenchmarkProperty connectionsBefore =
            new BenchmarkProperty("connectionsBefore", connection.g().V(supernode).outE().count());
        isvb.collectBenchmarkProperty(connectionsBefore, Tracking.BEFORE);
        final BenchmarkProperty connectionsAfter =
            new BenchmarkProperty("connectionsAfter", connection.g().V(supernode).outE().count());
        isvb.collectBenchmarkProperty(connectionsAfter, Tracking.AFTER);

        // create edge existence checker
        final String supernodeName = (String)connection.g().V(supernode).values("name").next();
        final EdgeExistenceBenchmark<String> eeb = new EdgeExistenceBenchmark<String>(
            connection, supernode, "name", new String[] {supernodeName});
        eeb.setUseEdgeIndex(true);
        final BenchmarkProperty connections =
            new BenchmarkProperty("connections", connection.g().V(supernode).outE().count());
        eeb.collectBenchmarkProperty(connections, Tracking.AFTER);

        // compose benchmark
        final ComposedBenchmark cb = new ComposedBenchmark(connection, runs);
        cb.setCollectResults(false);
        cb.addComponent(isvb, stepsPerRun);
        cb.addComponent(eeb);

        addComponent(cb);
    }
}