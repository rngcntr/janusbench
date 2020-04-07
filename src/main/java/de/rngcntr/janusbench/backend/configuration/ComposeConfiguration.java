package de.rngcntr.janusbench.backend.configuration;

import de.rngcntr.janusbench.backend.Index;
import de.rngcntr.janusbench.backend.Storage;
import java.io.File;
import org.testcontainers.containers.ContainerLaunchException;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class ComposeConfiguration extends Configuration {

    private DockerComposeContainer<?> environment;

    /**
     * Initializes a new ComposeConfiguration from a given storage and index backend.
     * 
     * @param storage The storage backend to use.
     * @param index The index backend to use.
     */
    public ComposeConfiguration(Storage storage, Index index) {
        super(storage, index);
    }

    /**
     * Initializes a new ComposeConfiguration from a given storage backend without an index backend.
     * 
     * @param storage The storage backend to use.
     */
    public ComposeConfiguration(Storage storage) {
        super(storage);
    }

    private DockerComposeContainer<?> getEnvironment() {
        File composeFile = new File(getPath());
        DockerComposeContainer<?> environment = new DockerComposeContainer<>(composeFile);
        return environment;
    }

    /**
     * Starts this configuration as a Docker Compose environment.
     * 
     * @return <ul><li>true if the environment was started successfully</li><li>false if not</li></ul>
     */
    public boolean start() {
        if (environment != null) {
            environment.stop();
        }

        // watch out for differently named container when using foundationdb backend
        String janusgraphService = "janusgraph";

        environment =
            getEnvironment()
                .withExposedService(
                    janusgraphService, JANUSGRAPH_PORT,
                    Wait.forListeningPort().withStartupTimeout(timeoutDuration))
                .waitingFor(janusgraphService, Wait.forLogMessage(".*Channel started at port.*", 1))
                .withLocalCompose(true);

        try {
            environment.start();
            return true;
        } catch (final ContainerLaunchException clex) {
            log.error("docker-compose environment could not be started.");
            log.error(clex);
            return false;
        }
    }

    /**
     * Stops the Docker Compose environment.
     */
    public void stop() {
        if (environment != null) {
            environment.close();
        }
    }
}