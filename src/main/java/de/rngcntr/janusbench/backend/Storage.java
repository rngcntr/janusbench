package de.rngcntr.janusbench.backend;

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