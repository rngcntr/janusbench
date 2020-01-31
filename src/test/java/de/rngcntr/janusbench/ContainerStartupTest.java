package de.rngcntr.janusbench;

import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ContainerStartupTest {

    private static int JG_PORT = 8182;

    private static File composeFile = new File("janusgraph-inmemory.yml");

    @ClassRule
    public static DockerComposeContainer environment = new DockerComposeContainer(composeFile)
            .withExposedService("janusgraph", JG_PORT)
            .waitingFor("janusgraph", Wait.forLogMessage("Channel started at port " + JG_PORT, 1));

    @Test
    public void testContainerStartup() {
        //environment.starting(Description.createTestDescription(Object.class, "name"));
        environment.start();
        assertNotNull("JanusGraph server started", environment.getServicePort("janusgraph", JG_PORT));
    }
}