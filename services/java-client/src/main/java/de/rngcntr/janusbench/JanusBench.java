package de.rngcntr.janusbench;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.apache.tinkerpop.gremlin.driver.exception.ResponseException;
import org.apache.commons.configuration.PropertiesConfiguration;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

public class JanusBench {

    private static final String REMOTE_PROPERTIES = "conf/remote-graph.properties";
    private static final String INIT_SCRIPT = "conf/initialize-graph.groovy";

    private static final Logger log = Logger.getLogger(JanusBench.class);

    private static GraphTraversalSource g;
    private final String propertiesFileName;
    private PropertiesConfiguration conf;
    private Cluster cluster;
    private Client client;

    public JanusBench(final String propertiesFileName) {
        this.propertiesFileName = propertiesFileName;
    }

    public void runBenchmarks() {
        client.submit(g.V().drop());
        client.submit(g.addV());
        client.submit(g.addV());
        client.submit(g.addV());
        final ResultSet result = client.submit(g.V().count());
        System.out.println(result.one().getInt());
    }

    public void createSchema(final String initFileName) throws IOException {
        log.info("Creating schema");
        final String initRequest = new String(Files.readAllBytes(Paths.get(initFileName)));
        client.submit(initRequest);
        log.info("Done creating schema");
    }

    public void openGraph() throws ConfigurationException {
        log.info("Opening graph");
        conf = new PropertiesConfiguration(propertiesFileName);

        try {
            cluster = Cluster.open(conf.getString("gremlin.remote.driver.clusterFile"));
            client = cluster.connect();
            client = client.alias("g");
            g = traversal().withRemote(propertiesFileName);
        } catch (final Exception ex) {
            throw new ConfigurationException(ex);
        }
    }

    public void closeGraph () throws Exception {
        try {
            log.info("Closing graph");
            g.close();
            client.close();
            cluster.close();
            log.info("Successfully closed graph");
        } finally {
            g = null;
            client = null;
            cluster = null;
        }
    }

    public static void main(final String[] args) {
        System.out.println("This is JanusBench v0.0.1");

        final JanusBench jb = new JanusBench(REMOTE_PROPERTIES);

        try {
            jb.openGraph();
        } catch (final ConfigurationException cex) {
            log.error("Unable to connect to graph");
            log.error(cex);
        }

        try {
            jb.createSchema(INIT_SCRIPT);
        } catch (IOException ioex) {
            log.error("Graph initialization script not found: " + INIT_SCRIPT);
        }

        jb.runBenchmarks();

        try {
            jb.closeGraph();
        } catch (Exception ex) {
            log.error("Unable to close graph");
            log.error(ex);
        }
    }
}
