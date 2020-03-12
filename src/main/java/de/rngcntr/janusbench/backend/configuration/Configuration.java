package de.rngcntr.janusbench.backend.configuration;

import de.rngcntr.janusbench.backend.Index;
import de.rngcntr.janusbench.backend.Storage;
import de.rngcntr.janusbench.exceptions.InvalidConfigurationException;
import java.io.File;
import java.time.Duration;
import org.apache.log4j.Logger;

public abstract class Configuration {

    protected static final String CONFIG_BASE_PATH = "docker/configurations/";
    protected static final String FILE_EXTENSION = ".yml";
    protected static final int JANUSGRAPH_PORT = 8182;

    protected static final Logger log = Logger.getLogger(Configuration.class);

    protected Storage storage;
    protected Index index;

    protected Duration timeoutDuration;

    /**
     * Initializes a new Configuration from a given storage and index backend.
     *
     * @param storage The storage backend to use.
     * @param index The index backend to use.
     */
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

    /**
     * Initializes a new Configuration from a given storage backend without an index backend.
     *
     * @param storage The storage backend to use.
     */
    public Configuration(Storage storage) {
        if (storage == null) {
            throw new InvalidConfigurationException(String.format("A storage backend is required"));
        }

        this.storage = storage;
        this.index = null;
    }

    /**
     * Sets the maximum duration before the configuration startup will abort.
     *
     * @param timeoutDuration The maximum time to wait.
     */
    public void setTimeout(Duration timeoutDuration) { this.timeoutDuration = timeoutDuration; }

    /**
     * Starts this configuration.
     *
     * @return <ul><li>true if the environment was started successfully</li><li>false if
     *     not</li></ul>
     */
    public abstract boolean start();

    /**
     * Stops this environment.
     */
    public abstract void stop();

    /**
     * Returns the path of the corresponding Docker Compose file
     *
     * @return The path of the Docker Compose file for this configuration.
     */
    public String getPath() {
        String fileName = "janusgraph";
        fileName += (storage != null ? "-" + storage : "");
        fileName += (index != null ? "-" + index : "");
        fileName += FILE_EXTENSION;
        return CONFIG_BASE_PATH + fileName;
    }

    /**
     * Returns the storage backend used in this configuration.
     *
     * @return The used storage backend.
     */
    public Storage getStorage() { return storage; }

    /**
     * Returns the index backend used in this configuration. Returns <code>null</code> if no
     * index backend is used.
     *
     * @return The used index backend.
     */
    public Index getIndex() { return index; }
}
