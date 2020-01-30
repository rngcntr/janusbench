package de.rngcntr.janusbench;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import de.rngcntr.janusbench.benchmark.complex.IndexedEdgeExistenceOnSupernode;
import de.rngcntr.janusbench.benchmark.simple.EdgeExistenceBenchmark;
import de.rngcntr.janusbench.benchmark.simple.InsertSupernodeVerticesBenchmark;
import de.rngcntr.janusbench.benchmark.simple.InsertVerticesBenchmark;
import de.rngcntr.janusbench.tinkerpop.Connection;
import de.rngcntr.janusbench.util.Benchmark;
import de.rngcntr.janusbench.util.BenchmarkProperty;
import de.rngcntr.janusbench.util.BenchmarkResult;
import de.rngcntr.janusbench.util.ComposedBenchmark;
import de.rngcntr.janusbench.util.ResultLogger;
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

    public Connection gConnection() {
        return connection;
    }

    public void runBenchmark(Benchmark benchmark) {
        benchmark.run();
        for (BenchmarkResult br : benchmark.getResults()) {
            System.out.println(br);
        }
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

            try {
                ResultLogger.getInstance().setOutputMethod("results.txt");
            } catch (IOException e) {
                log.error("Unable to create results file");
                e.printStackTrace();
            }

            jb.runBenchmark(new IndexedEdgeExistenceOnSupernode(jb.gConnection(), 5, 10, 1000));
        }

        jb.closeGraph();
        ResultLogger.getInstance().terminate();
    }
}
