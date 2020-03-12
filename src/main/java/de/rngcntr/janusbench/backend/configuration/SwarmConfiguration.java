package de.rngcntr.janusbench.backend.configuration;

import de.rngcntr.janusbench.backend.Index;
import de.rngcntr.janusbench.backend.Storage;
import java.time.Duration;
import java.time.LocalDateTime;

public class SwarmConfiguration extends Configuration {

    private static final int RETRY_MILLIS = 1000;

    /**
     * Initializes a new SwarmConfiguration from a given storage and index backend.
     *
     * @param storage The storage backend to use.
     * @param index The index backend to use.
     */
    public SwarmConfiguration(Storage storage, Index index) { super(storage, index); }

    /**
     * Initializes a new SwarmConfiguration from a given storage backend without an index backend.
     *
     * @param storage The storage backend to use.
     */
    public SwarmConfiguration(Storage storage) { super(storage); }

    /**
     * Starts this configuration as a Docker Swarm environment.
     *
     * @return <ul><li>true if the environment was started successfully</li><li>false if
     *     not</li></ul>
     */
    public boolean start() {
        LocalDateTime startTime = LocalDateTime.now();

        while (Duration.between(startTime, LocalDateTime.now()).compareTo(timeoutDuration) < 0) {
            if (isUp()) {
                return true;
            }
            try {
                Thread.sleep(RETRY_MILLIS);
            } catch (InterruptedException iex) {
                return false;
            }
        }

        return false;
    }

    private boolean isUp() {
        return false;
    }

    /**
     * Stops the Docker Swarm environment.
     */
    public void stop() {
    }
}