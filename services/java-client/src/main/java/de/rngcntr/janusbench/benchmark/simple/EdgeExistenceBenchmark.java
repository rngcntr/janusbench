package de.rngcntr.janusbench.benchmark.simple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.graphdb.types.system.ImplicitKey;

import de.rngcntr.janusbench.tinkerpop.Connection;
import de.rngcntr.janusbench.util.Benchmark;
import de.rngcntr.janusbench.util.BenchmarkResult;
import de.rngcntr.janusbench.util.BenchmarkProperty;

public class EdgeExistenceBenchmark<T> extends Benchmark {

    private Vertex supernode;
    private String propertyName;
    private T[] nodeProperties;
    private boolean useEdgeIndex;

    private String vertexExistenceQuery;
    private String defaultEdgeExistenceQuery;
    private String indexedEdgeExistenceQuery;

    private Map<String, Object> vertexExistenceParameters;
    private Map<String, Object> edgeExistenceParameters;

    public EdgeExistenceBenchmark(Connection connection, Vertex supernode, String propertyName, T[] nodeProperties) {
        super(connection, nodeProperties.length);
        this.supernode = supernode;
        this.propertyName = propertyName;
        this.nodeProperties = nodeProperties;
        this.useEdgeIndex = true;
    }

    public void setUseEdgeIndex(boolean useEdgeIndex) {
        this.useEdgeIndex = useEdgeIndex;
    }

    @Override
    public void buildUp() {
        vertexExistenceQuery = "g.V()."
            + "has(searchKey, searchValue)."
            + "limit(1)."
            + "toList()";
        defaultEdgeExistenceQuery = "g.V(supernode)."
            + "out('knows')."
            + "hasId(adjacentId)."
            + "limit(1)."
            + "hasNext()";
        indexedEdgeExistenceQuery = "g.V(supernode)."
            + "outE('knows')."
            + "has(searchKey, adjacentId)."
            + "limit(1)."
            + "hasNext()";

        vertexExistenceParameters = new HashMap<String, Object>();
        edgeExistenceParameters = new HashMap<String, Object>();
    }

    @Override
    public void performAction(BenchmarkResult result) {
        BenchmarkProperty useEdgeIndexProperty = new BenchmarkProperty("useEdgeIndex", useEdgeIndex);
        result.injectBenchmarkProperty(useEdgeIndexProperty);

        vertexExistenceParameters.put("searchKey", propertyName);
        edgeExistenceParameters.put("supernode", supernode);

        if (useEdgeIndex) {
            edgeExistenceParameters.put("searchKey", ImplicitKey.ADJACENT_ID.name());

            for (int index = 0; index < stepSize; ++index) {
                vertexExistenceParameters.put("searchValue", nodeProperties[index]);
                ResultSet candidates = connection.submitAsync(vertexExistenceQuery, vertexExistenceParameters);
                Vertex testNode = candidates.one().getVertex();
                if (testNode != null) {
                    edgeExistenceParameters.put("adjacentId", testNode.id());
                    connection.submit(indexedEdgeExistenceQuery, edgeExistenceParameters);
                }
            }
        } else {
            for (int index = 0; index < stepSize; ++index) {
                vertexExistenceParameters.put("searchValue", nodeProperties[index]);
                ResultSet candidates = connection.submitAsync(vertexExistenceQuery, vertexExistenceParameters);
                Vertex testNode = candidates.one().getVertex();
                if (testNode != null) {
                    edgeExistenceParameters.put("adjacentId", testNode.id());
                    connection.submit(defaultEdgeExistenceQuery, edgeExistenceParameters);
                }
            }
        }
    }

    @Override
    public void tearDown() {}
}
