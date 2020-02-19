package de.rngcntr.janusbench.backend;

import de.rngcntr.janusbench.exceptions.InvalidConfigurationException;
import java.io.File;
import org.apache.log4j.Logger;
import org.testcontainers.containers.ContainerLaunchException;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class Configuration {

    private static final String CONFIG_BASE_PATH = "docker/configurations/";
    private static final String FILE_EXTENSION = ".yml";
    private static final int JANUSGRAPH_PORT = 8182;

    private static final Logger log = Logger.getLogger(Configuration.class);

    private Storage storage;
    private Index index;

    private DockerComposeContainer<?> environment;

    public Configuration(Storage storage, Index index) {
        if (storage == null) {
            throw new InvalidConfigurationException(String.format("A storage backend is required"));
        }

        this.storage = storage;
        this.index = index;

        if (!new File(getPath()).exists()) {
            throw new InvalidConfigurationException(
                String.format("Storage %s is not compatible with index %s", storage, index));
        }
    }

    public Configuration(Storage storage) {
        if (storage == null) {
            throw new InvalidConfigurationException(String.format("A storage backend is required"));
        }

        this.storage = storage;
        this.index = null;
    }

    private DockerComposeContainer<?> getEnvironment() {
        File composeFile = new File(getPath());
        DockerComposeContainer<?> environment = new DockerComposeContainer<>(composeFile);
        return environment;
    }

    public boolean start() {
        if (environment != null) {
            environment.stop();
        }

        environment =
            getEnvironment()
                .withExposedService("janusgraph", JANUSGRAPH_PORT)
                .waitingFor("janusgraph", Wait.forLogMessage(".*Channel started at port.*", 1))
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

    public void stop() {
        if (environment != null) {
            environment.stop();
        }
    }

    public String getPath() {
        String fileName = "janusgraph";
        fileName += (storage != null ? "-" + storage : "");
        fileName += (index != null ? "-" + index : "");
        fileName += FILE_EXTENSION;
        return CONFIG_BASE_PATH + fileName;
    }
}