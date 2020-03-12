package de.rngcntr.janusbench.backend.configuration;

import de.rngcntr.janusbench.backend.Index;
import de.rngcntr.janusbench.backend.Storage;

public class SwarmConfiguration extends Configuration {

    /**
     * Initializes a new SwarmConfiguration from a given storage and index backend.
     * 
     * @param storage The storage backend to use.
     * @param index The index backend to use.
     */
    public SwarmConfiguration(Storage storage, Index index) {
        super(storage, index);
    }

    /**
     * Initializes a new SwarmConfiguration from a given storage backend without an index backend.
     * 
     * @param storage The storage backend to use.
     */
    public SwarmConfiguration(Storage storage) {
        super(storage);
    }

    /**
     * Starts this configuration as a Docker Compose environment.
     * 
     * @return <ul><li>true if the environment was started successfully</li><li>false if not</li></ul>
     */
    public boolean start() {
        return false;
    }

    /**
     * Stops the Docker Compose environment.
     */
    public void stop() {
    }
}