package de.rngcntr.janusbench.benchmark.composed;

import de.rngcntr.janusbench.benchmark.helper.InsertSupernodeVerticesBenchmark;
import de.rngcntr.janusbench.benchmark.helper.InsertVerticesBenchmark;
import de.rngcntr.janusbench.util.BenchmarkProperty;
import de.rngcntr.janusbench.util.BenchmarkProperty.Tracking;
import de.rngcntr.janusbench.util.ComposableBenchmark;
import de.rngcntr.janusbench.util.ComposedBenchmark;
import de.rngcntr.janusbench.util.MicroBenchmark;
import java.util.HashMap;
import java.util.Map;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public class EdgeCountOnSupernode extends ComposableBenchmark {

    public int runs;
    public int stepsPerRun;
    public int edgesPerStep;
    public String approach;

    public EdgeCountOnSupernode() {
        super();
    }

    @Override
    public void buildUp() {
        // prepare a vertex to later become a supernode
        final InsertVerticesBenchmark ivb = new InsertVerticesBenchmark(connection, 1);
        ivb.run();
        final Vertex supernode = connection.g().V().next();

        // create the inserter benchmark
        final InsertSupernodeVerticesBenchmark isvb =
            new InsertSupernodeVerticesBenchmark(connection, edgesPerStep, supernode);
        
        final int[] insertedVertices = {-edgesPerStep};
        final BenchmarkProperty connectionsBefore =
            new BenchmarkProperty("connectionsBefore", (c) -> insertedVertices[0] += edgesPerStep);
        isvb.collectBenchmarkProperty(connectionsBefore, Tracking.BEFORE);

        String traversal = "g.V(supernode).outE().count().next()";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("supernode", supernode);
        final MicroBenchmark ecb = new MicroBenchmark(connection, traversal, parameters);

        final BenchmarkProperty connections =
            new BenchmarkProperty("connections", (c) -> insertedVertices[0] + edgesPerStep);
        ecb.collectBenchmarkProperty(connections, Tracking.AFTER);

        // compose benchmark
        final ComposedBenchmark cb = new ComposedBenchmark(connection, runs);
        cb.setCollectResults(false);
        cb.addComponent(isvb, stepsPerRun);
        cb.addComponent(ecb);

        addComponent(cb);
    }
}