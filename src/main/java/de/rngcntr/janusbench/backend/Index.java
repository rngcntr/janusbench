package de.rngcntr.janusbench.backend;

public enum Index {
    ELASTICSEARCH,
    SOLR,
    LUCENE;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}