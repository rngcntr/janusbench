package de.rngcntr.janusbench.backend;

/**
 * The <code>Index</code> enum manages the available options for index backends.
 * Every {@link de.rngcntr.janusbench.backend.configuration.Configuration} uses up to one of these to store and manage graph indexes.
 * 
 * If new index backends are implemented in JanusGraph, this enum needs to be extended by the classifiers of the new backends.
 * 
 * When running <code>janusbench</code>, all values of <code>Index</code> are valid options for the parameter <code>-i</code> or <code>--index</code>.
 * 
 * @author Florian Grieskamp
 */
public enum Index {
    NONE,
    ELASTICSEARCH,
    SOLR,
    LUCENE;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}