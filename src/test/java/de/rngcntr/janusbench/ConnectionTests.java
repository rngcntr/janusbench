package de.rngcntr.janusbench;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import de.rngcntr.janusbench.tinkerpop.Connection;

@Testcontainers
public class ConnectionTests {

    private static int JG_PORT = 8182;

    private static File composeFile = new File("docker/configurations/janusgraph-inmemory.yml");

    private static final String REMOTE_PROPERTIES = "conf/remote-graph.properties";

    @Container
    public static DockerComposeContainer<?> environment = new DockerComposeContainer<>(composeFile)
            .withExposedService("janusgraph", JG_PORT)
            .waitingFor("janusgraph", Wait.forLogMessage(".*Channel started at port.*", 1)).withLocalCompose(true);

    @Test
    public void testEstablishValidConnection() throws Exception {
        Connection conn = new Connection(REMOTE_PROPERTIES);
        conn.open();
        assertNotNull("Graph traversal source should be available", conn.g());
        conn.close();
    }

    @Test
    public void testEstablishInvalidConnection() {
        Connection conn = new Connection("/dev/null");
        Exception exception = assertThrows(ConfigurationException.class, () -> conn.open());
 
        assertTrue("ConfigurationException should be thrown", exception instanceof ConfigurationException);
    }

    @Test
    public void testEstablishValidConnectionWhenStopped() {
        environment.stop();
        Connection conn = new Connection(REMOTE_PROPERTIES);
        conn.setTimeout(3000);
        Exception exception = assertThrows(ConfigurationException.class, () -> conn.open());

        assertTrue("ConfigurationException should be thrown", exception instanceof ConfigurationException);
        assertTrue("ConfigurationException should contain unreachable statement",
                exception.getMessage().contains("Unable to reach cluster"));
    }
}