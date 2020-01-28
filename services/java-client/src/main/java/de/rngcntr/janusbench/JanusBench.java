package de.rngcntr.janusbench;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import de.rngcntr.janusbench.tinkerpop.Connection;

import org.apache.tinkerpop.gremlin.driver.ResultSet;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

public class JanusBench {

    private static final String REMOTE_PROPERTIES = "conf/remote-graph.properties";
    private static final String INIT_SCRIPT = "conf/initialize-graph.groovy";

    private static final Logger log = Logger.getLogger(JanusBench.class);

    private final Connection client;

    public JanusBench(final String propertiesFileName) {
        this.client = new Connection(propertiesFileName);
    }

    public void runBenchmarks() {
        client.commit(client.g().V().drop());
        client.commit(client.g().addV());
        client.commit(client.g().addV());
        client.commit(client.g().addV());
        final ResultSet result = client.commit(client.g().V().count());
        System.out.println(result.one().getInt());
    }

    public void createSchema(final String initFileName) throws IOException {
        log.info("Creating schema");
        final String initRequest = new String(Files.readAllBytes(Paths.get(initFileName)));
        client.commit(initRequest);
        log.info("Done creating schema");
    }

    public void openGraph() {
        log.info("Opening graph");
        try {
            client.open();
        } catch (final ConfigurationException cex) {
            log.error("Unable to connect to graph");
            log.error(cex);
        }
        log.info("Successfully opened graph");
    }

    public void closeGraph() {
        log.info("Closing graph");
        try {
            client.close();
        } catch (final Exception ex) {
            log.error("Unable to close graph");
            log.error(ex);
        }
        log.info("Successfully closed graph");
    }

    public static void main(final String[] args) {
        System.out.println("This is JanusBench v0.0.1");

        final JanusBench jb = new JanusBench(REMOTE_PROPERTIES);

        jb.openGraph();

        try {
            jb.createSchema(INIT_SCRIPT);
        } catch (final IOException ioex) {
            log.error("Graph initialization script not found: " + INIT_SCRIPT);
        }

        jb.runBenchmarks();

        jb.closeGraph();
    }
}
