package de.rngcntr.janusbench;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import de.rngcntr.janusbench.benchmark.simple.EdgeExistenceBenchmark;
import de.rngcntr.janusbench.benchmark.simple.InsertSupernodeVerticesBenchmark;
import de.rngcntr.janusbench.benchmark.simple.InsertVerticesBenchmark;
import de.rngcntr.janusbench.tinkerpop.Connection;
import de.rngcntr.janusbench.util.BenchmarkProperty;
import de.rngcntr.janusbench.util.ComposedBenchmark;
import de.rngcntr.janusbench.util.BenchmarkProperty.Tracking;

import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

public class JanusBench {

    private static final String REMOTE_PROPERTIES = "conf/remote-graph.properties";
    private static final String INIT_SCRIPT = "conf/initialize-graph.groovy";

    private static final Logger log = Logger.getLogger(JanusBench.class);

    private final Connection connection;

    public JanusBench(final String propertiesFileName) {
        this.connection = new Connection(propertiesFileName);
    }

    public void runBenchmarks() {
        // prepare a vertex to later become a supernode
        InsertVerticesBenchmark ivb = new InsertVerticesBenchmark(connection, 1);
        ivb.run();
        Vertex supernode = connection.g().V().next();

        // create the inserter benchmark
        InsertSupernodeVerticesBenchmark isvb = new InsertSupernodeVerticesBenchmark(connection, 1000, supernode);
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
        ComposedBenchmark cb = new ComposedBenchmark(connection, 10);
        cb.addComponent(isvb, 10);
        cb.addComponent(eeb);

        // run it
        cb.run();
    }

    public void createSchema(final String initFileName) throws IOException {
        log.info("Creating schema");
        final String initRequest = new String(Files.readAllBytes(Paths.get(initFileName)));
        connection.submit(initRequest);
        log.info("Done creating schema");
    }

    public boolean openGraph() {
        log.info("Opening graph");
        
        try {
            connection.open();
        } catch (final ConfigurationException cex) {
            log.error("Unable to connect to graph");
            log.error(cex);
            return false;
        }

        log.info("Successfully opened graph");
        return true;
    }

    public boolean closeGraph() {
        log.info("Closing graph");

        try {
            connection.close();
        } catch (final Exception ex) {
            log.error("Unable to close graph");
            log.error(ex);
            return false;
        }

        log.info("Successfully closed graph");
        return true;
    }

    public static void main(final String[] args) {
        System.out.println("This is JanusBench v0.0.1");

        final JanusBench jb = new JanusBench(REMOTE_PROPERTIES);

        boolean open = jb.openGraph();

        if (open) {
            try {
                jb.createSchema(INIT_SCRIPT);
            } catch (final IOException ioex) {
                log.error("Graph initialization script not found: " + INIT_SCRIPT);
            }

            jb.runBenchmarks();
        }

        jb.closeGraph();
    }
}
