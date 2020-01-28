package de.rngcntr.janusbench.tinkerpop;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

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

    public Connection (String propertiesFileName) {
        this.propertiesFileName = propertiesFileName;
    }

    public void open () throws ConfigurationException {
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

    public ResultSet commit (String traversal) {
        return client.submit(traversal);
    }

    public ResultSet commit (GraphTraversal<?, ?> traversal) {
        return client.submit(traversal);
    }
}