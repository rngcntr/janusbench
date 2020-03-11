package de.rngcntr.janusbench.backend;

/**
 * The <code>storage</code> enum manages the available options for storage backends.
 * Every {@link de.rngcntr.janusbench.backend.Configuration} uses exactly one of these to store the graph data.
 * 
 * If new storage backends are implemented in JanusGraph, this enum needs to be extended by the classifiers of the new backends.
 * 
 * When running <code>janusbench</code>, all values of <code>Storage</code> are valid options for the parameter <code>-s</code> or <code>--storage</code>.
 * 
 * @author Florian Grieskamp
 */
public enum Storage {
    CASSANDRA,
    SCYLLA,
    BERKELEYJE,
    HBASE,
    YUGABYTE,
    FOUNDATIONDB,
    INMEMORY;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}