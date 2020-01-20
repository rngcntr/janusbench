package de.rngcntr.janusbench;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource;
import org.apache.log4j.Logger;

import de.rngcntr.janusbench.benchmark.InsertVerticesBenchmark;
import de.rngcntr.janusbench.benchmark.BenchmarkResult;

public class JanusBench {

    private static final String REMOTE_PROPERTIES = "conf/remote-graph.properties";
    private static final Logger log = Logger.getLogger(JanusBench.class);

    private static GraphTraversalSource g;

    public static void main (String[] args) {
        System.out.println("This is JanusBench v0.0.1");

        initializeConnection(REMOTE_PROPERTIES);

        log.info("Inserting Vertices");
        InsertVerticesBenchmark ivb = new InsertVerticesBenchmark(g, 10);
        ivb.run();
        log.info(ivb.getResults());

        try {
            g.close();
        } catch (Exception e) {
            log.error("Unable to close connection to JanusGraph");
        }
    }

    private static void initializeConnection (String propertiesFile) {
        try {
            g = AnonymousTraversalSource.traversal().withRemote(propertiesFile);
        } catch (Exception e) {
            log.error("Unable open connection to JanusGraph");
        }
    }

}
