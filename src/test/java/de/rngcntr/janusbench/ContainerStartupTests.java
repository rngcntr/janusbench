package de.rngcntr.janusbench;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.rngcntr.janusbench.subcommands.RunSubcommand;
import de.rngcntr.janusbench.util.ResultLogger;
import java.io.File;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ContainerStartupTests {

    private static int JG_PORT = 8182;

    private static File composeFile = new File("docker/configurations/janusgraph-inmemory.yml");

    // private static final String REMOTE_PROPERTIES = "conf/remote-graph.properties";

    @Container
    public static DockerComposeContainer<?> environment =
        new DockerComposeContainer<>(composeFile)
            .withExposedService("janusgraph", JG_PORT)
            .waitingFor("janusgraph", Wait.forLogMessage(".*Channel started at port.*", 1))
            .withLocalCompose(true);

    @Test
    public void testContainerStartup() {
        assertNotNull("JanusGraph server started",
                      environment.getServicePort("janusgraph", JG_PORT));
    }

    @Test
    public void testContainerStartupWithJanusBench() {
        final RunSubcommand runner = new RunSubcommand();

        final boolean open = runner.openGraph();

        assertTrue("Graph connection should be open", open);

        runner.closeGraph();
        ResultLogger.getInstance().terminate();
    }
}