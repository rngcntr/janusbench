package de.rngcntr.janusbench.benchmark.helper;

import de.rngcntr.janusbench.backend.Connection;
import de.rngcntr.janusbench.util.Benchmark;
import de.rngcntr.janusbench.util.BenchmarkProperty;
import de.rngcntr.janusbench.util.BenchmarkProperty.Tracking;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.graphdb.types.system.ImplicitKey;

/**
 * Receives a supernode with many neighbours and uses one of the available approaches to check
 * whether or not the supernode has an outgoing edge that points to a vertex with the given property
 * value.
 */
public class EdgeExistenceBenchmark<T> extends Benchmark {

    private final Vertex supernode;
    private final String propertyName;
    private final T[] nodeProperties;

    private String vertexExistenceQuery;
    private String directEdgeExistenceQuery;
    private String indirectEdgeExistenceQuery;
    private String naiveEdgeExistenceQuery;

    private Map<String, Object> vertexExistenceParameters;
    private Map<String, Object> edgeExistenceParameters;

    private Approach approach;

    public EdgeExistenceBenchmark(final Connection connection, final Vertex supernode,
                                  final String propertyName, final T[] nodeProperties) {
        super(connection, nodeProperties.length);
        this.supernode = supernode;
        this.propertyName = propertyName;
        this.nodeProperties = nodeProperties;
        approach = Approach.DIRECT_ID;

        if (nodeProperties.length != stepSize) {
            throw new IllegalArgumentException("Must receive one node property per step");
        }

        BenchmarkProperty useIndexProperty = new BenchmarkProperty(
            "approach", (c) -> approach.toString().toLowerCase().replace("_", "-"));
        collectBenchmarkProperty(useIndexProperty, Tracking.BEFORE);
    }

    public enum Approach {
        DIRECT_ID,
        INDIRECT_ID,
        NAIVE_PROPERTY
    }

    /**
     * When querying the existence of an edge between two vertices there are two options:
     * <ul>
     * <li> From one vertex, traverse every adjacent vertex and compare it to the one you are
     * looking for (<code>INDIRECT_ID</code>)</li> <li> Lookup the ID of one vertex and then
     * traverse all edges of the other one, checking each one for the known ID
     * (<code>DIRECT_ID</code>)</li>
     * </ul>
     * Obviously, the second option is much faster, as there is no need to fetch all adjacent
     * vertices from the database. Starting with JanusGraph 0.5.0, the choice of
     * between ADJACENT_ID and ADJACENT_VERTEX will not make a difference anymore as the queries are
     * automatically optimized to use the second approach.
     * 
     * However, the naive approach will still result in a massive runtime penalty.
     *
     * @param useIndex Whether or not to use the index on adjacent ids.
     */
    public void useApproach(Approach approach) {
        this.approach = approach;
    }

    @Override
    public void buildUp() {
        vertexExistenceQuery = "g.V()."
                               + "has(searchKey, searchValue)."
                               + "limit(1)."
                               + "toList()";
        directEdgeExistenceQuery = "g.V(supernode)."
                                   + "outE('knows')."
                                   + "has(searchKey, adjacentId)."
                                   + "limit(1)."
                                   + "hasNext()";
        indirectEdgeExistenceQuery = "g.V(supernode)."
                                     + "out('knows')."
                                     + "hasId(adjacentId)."
                                     + "limit(1)."
                                     + "hasNext()";
        naiveEdgeExistenceQuery = "g.V(supernode)."
                                  + "out('knows')."
                                  + "has(searchKey, searchValue)."
                                  + "limit(1)."
                                  + "hasNext()";

        vertexExistenceParameters = new HashMap<String, Object>();
        edgeExistenceParameters = new HashMap<String, Object>();
    }

    @Override
    public void performAction() throws TimeoutException {
        vertexExistenceParameters.put("searchKey", propertyName);
        edgeExistenceParameters.put("supernode", supernode);

        switch (approach) {
        case DIRECT_ID:
            for (int index = 0; index < stepSize; ++index) {
                doDirectLookup(nodeProperties[index]);
            }
            break;
        case INDIRECT_ID:
            for (int index = 0; index < stepSize; ++index) {
                doIndirectLookup(nodeProperties[index]);
            }
            break;
        case NAIVE_PROPERTY:
            edgeExistenceParameters.put("searchKey", propertyName);
            for (int index = 0; index < stepSize; ++index) {
                doNaiveLookup(nodeProperties[index]);
            }
            break;
        }
    }

    @Override
    public void tearDown() {}

    private void doDirectLookup(T nodeProperty) throws TimeoutException {
        vertexExistenceParameters.put("searchValue", nodeProperty);
        ResultSet candidates =
            connection.submitAsync(vertexExistenceQuery, vertexExistenceParameters);
        final Vertex testNode = candidates.one().getVertex();
        if (testNode != null) {
            edgeExistenceParameters.put("adjacentId", testNode.id());
            edgeExistenceParameters.put("searchKey", ImplicitKey.ADJACENT_ID.name());
            connection.submit(directEdgeExistenceQuery, edgeExistenceParameters);
        }
    }

    private void doIndirectLookup(T nodeProperty) throws TimeoutException {
        vertexExistenceParameters.put("searchValue", nodeProperty);
        ResultSet candidates =
            connection.submitAsync(vertexExistenceQuery, vertexExistenceParameters);
        final Vertex testNode = candidates.one().getVertex();
        if (testNode != null) {
            edgeExistenceParameters.put("adjacentId", testNode.id());
            connection.submit(indirectEdgeExistenceQuery, edgeExistenceParameters);
        }
    }

    private void doNaiveLookup(T nodeProperty) throws TimeoutException {
        edgeExistenceParameters.put("searchValue", nodeProperty);
        connection.submit(naiveEdgeExistenceQuery, edgeExistenceParameters);
    }
}
