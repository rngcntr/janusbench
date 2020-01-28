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

    private final String propertiesFileName;
    private PropertiesConfiguration conf;

    private GraphTraversalSource g;
    private Cluster cluster;
    private Client client;
    private UUID sessionUuid;

    public Connection (String propertiesFileName) {
        this.propertiesFileName = propertiesFileName;
        sessionUuid = UUID.randomUUID();
    }

    public void open () throws ConfigurationException {
        conf = new PropertiesConfiguration(propertiesFileName);

        try {
            cluster = Cluster.open(conf.getString("gremlin.remote.driver.clusterFile"));
            client = cluster.connect(sessionUuid.toString(), false);
            client = client.alias("g");
            g = traversal().withRemote(propertiesFileName);
        } catch (final Exception ex) {
            throw new ConfigurationException(ex);
        }
    }

    public void close () throws Exception {
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

    public GraphTraversalSource g () {
        return g;
    }

    public ResultSet submit (String traversal) {
        return client.submit(traversal);
    }

    public ResultSet submit (String traversal, Map<String, Object> parameters) {
        return client.submit(traversal, parameters);
    }

    public ResultSet submit (GraphTraversal<?, ?> traversal) {
        return client.submit(traversal);
    }
}