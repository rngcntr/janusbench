package de.rngcntr.janusbench.benchmark.complex;

import org.apache.tinkerpop.gremlin.structure.Vertex;

import de.rngcntr.janusbench.benchmark.simple.EdgeExistenceBenchmark;
import de.rngcntr.janusbench.benchmark.simple.InsertSupernodeVerticesBenchmark;
import de.rngcntr.janusbench.benchmark.simple.InsertVerticesBenchmark;
import de.rngcntr.janusbench.tinkerpop.Connection;
import de.rngcntr.janusbench.util.BenchmarkProperty;
import de.rngcntr.janusbench.util.ComposedBenchmark;
import de.rngcntr.janusbench.util.BenchmarkProperty.Tracking;

public class IndexedEdgeExistenceOnSupernode extends ComposedBenchmark {

    public IndexedEdgeExistenceOnSupernode(Connection connection, int runs, int stepsPerRun, int edgesPerStep) {
        super(connection);

        // prepare a vertex to later become a supernode
        InsertVerticesBenchmark ivb = new InsertVerticesBenchmark(connection, 1);
        ivb.run();
        Vertex supernode = connection.g().V().next();

        // create the inserter benchmark
        InsertSupernodeVerticesBenchmark isvb = new InsertSupernodeVerticesBenchmark(connection, edgesPerStep, supernode);
        BenchmarkProperty connectionsBefore = new BenchmarkProperty("connectionsBefore",
                connection.g().V(supernode).outE().count());
        isvb.collectBenchmarkProperty(connectionsBefore, Tracking.BEFORE);
        BenchmarkProperty connectionsAfter = new BenchmarkProperty("connectionsAfter",
                connection.g().V(supernode).outE().count());
        isvb.collectBenchmarkProperty(connectionsAfter, Tracking.AFTER);

        // create edge existence checker
        String supernodeName = (String) connection.g().V(supernode).values("name").next();
        EdgeExistenceBenchmark<String> eeb = new EdgeExistenceBenchmark<String>(connection, supernode, "name", new String[] {supernodeName});
        eeb.setUseEdgeIndex(true);
        BenchmarkProperty connections = new BenchmarkProperty("connections", connection.g().V(supernode).outE().count());
        eeb.collectBenchmarkProperty(connections, Tracking.AFTER);

        // compose benchmark
        ComposedBenchmark cb = new ComposedBenchmark(connection, runs);
        cb.setCollectResults(false);
        cb.addComponent(isvb, stepsPerRun);
        cb.addComponent(eeb);
        
        addComponent(cb);
    }

}