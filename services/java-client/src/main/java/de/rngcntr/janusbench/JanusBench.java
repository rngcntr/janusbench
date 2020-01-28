package de.rngcntr.janusbench;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.apache.tinkerpop.gremlin.driver.exception.ResponseException;
import org.apache.commons.configuration.PropertiesConfiguration;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

public class JanusBench {

    private static final String REMOTE_PROPERTIES = "conf/remote-graph.properties";
    private static final Logger log = Logger.getLogger(JanusBench.class);

    private static GraphTraversalSource g;
    private final String propertiesFileName;
    private PropertiesConfiguration conf;
    private Cluster cluster;
    private Client client;

    public JanusBench(final String propertiesFileName) {
        this.propertiesFileName = propertiesFileName;
    }

    private void runBenchmarks() {
        client.submit(g.addV());
        ResultSet result = client.submit(g.V().count());
        System.out.println(result.one().getInt());
    }

    private void openGraph() throws ConfigurationException {
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

    private void closeGraph () throws ResponseException {
        log.info("Closing graph");
        client.close();
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

        jb.runBenchmarks();

        try {
            jb.closeGraph();
        } catch (final ResponseException rex) {
            log.error("Unable to close graph");
            log.error(rex);
        }

        System.exit(0);
    }
}
