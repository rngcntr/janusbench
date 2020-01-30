package de.rngcntr.janusbench.tinkerpop;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

import java.util.Map;
import java.util.UUID;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;

public class Connection {

    private final int TIMEOUT_MS = 60000;
    private final String propertiesFileName;
    private PropertiesConfiguration conf;

    private GraphTraversalSource g;
    private Cluster cluster;
    private Client client;
    private final UUID sessionUuid;

    public Connection(final String propertiesFileName) {
        this.propertiesFileName = propertiesFileName;
        sessionUuid = UUID.randomUUID();
    }

    public void open() throws ConfigurationException {
        conf = new PropertiesConfiguration(propertiesFileName);

        try {
            cluster = Cluster.open(conf.getString("gremlin.remote.driver.clusterFile"));
            client = cluster.connect(sessionUuid.toString(), false);
            client.alias("g");
            g = traversal().withRemote(propertiesFileName);
        } catch (final Exception ex) {
            throw new ConfigurationException(ex);
        }

        // wait for connection to become stable
        boolean connected = false;
        final long maxTime = System.currentTimeMillis() + TIMEOUT_MS;
        while (!connected && System.currentTimeMillis() < maxTime) {
            try {
                connected = client.submit("true").one().getBoolean();
            } catch (final RuntimeException rex) {
            }
        }

        if (!connected) {
            throw new ConfigurationException(String.format("Unable to reach cluster within %sms", TIMEOUT_MS));
        }
    }

    public void close() throws Exception {
        try {
            g.close();
            client.close();
            cluster.close();
        } finally {
            g = null;
            client = null;
            cluster = null;
        }
    }

    public GraphTraversalSource g() {
        return g;
    }

    public ResultSet submit(final String traversal) {
        return awaitResults(client.submit(traversal));
    }

    public ResultSet submitAsync(final String traversal, final Map<String, Object> parameters) {
        return client.submit(traversal, parameters);
    }

    public ResultSet submit(final String traversal, final Map<String, Object> parameters) {
        return awaitResults(client.submit(traversal, parameters));
    }

    public ResultSet submit(final GraphTraversal<?, ?> traversal) {
        return awaitResults(client.submit(traversal));

    }

    public ResultSet awaitResults(final ResultSet rs) {
        while (!rs.allItemsAvailable()) {
        }
        return rs;
    }
}
