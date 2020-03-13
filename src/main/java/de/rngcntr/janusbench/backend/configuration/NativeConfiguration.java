package de.rngcntr.janusbench.backend.configuration;

import de.rngcntr.janusbench.backend.Index;
import de.rngcntr.janusbench.backend.Storage;

public class NativeConfiguration extends Configuration {

    /**
     * Initializes a new NativeConfiguration from a given storage and index backend.
     *
     * @param storage The storage backend to use. This value is only used for logging purposes as
     *     the real backends are controlled externally by the user.
     * @param index The index backend to use. This value is only used for logging purposes as
     *     the real backends are controlled externally by the user.
     */
    public NativeConfiguration(Storage storage, Index index) { super(storage, index); }

    /**
     * Initializes a new NativeConfiguration from a given storage backend without an index backend.
     *
     * @param storage The storage backend to use. This value is only used for logging purposes as
     *     the real backends are controlled externally by the user.
     */
    public NativeConfiguration(Storage storage) { super(storage); }

    /**
     * There is no need to start this environment, because it's already running by definition.
     *
     * @return always <code>true</code>.
     */
    public boolean start() {
        return true;
    }

    /**
     * There is no need to stop this environment.
     */
    public void stop() {
    }
}