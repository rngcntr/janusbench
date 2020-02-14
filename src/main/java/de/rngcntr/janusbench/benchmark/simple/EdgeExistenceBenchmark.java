package de.rngcntr.janusbench.benchmark.simple;

import de.rngcntr.janusbench.backend.Connection;
import de.rngcntr.janusbench.util.Benchmark;
import de.rngcntr.janusbench.util.BenchmarkResult;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.graphdb.types.system.ImplicitKey;

public class EdgeExistenceBenchmark<T> extends Benchmark {

    private final Vertex supernode;
    private final String propertyName;
    private final T[] nodeProperties;

    private String vertexExistenceQuery;
    private String indexedEdgeExistenceQuery;

    private Map<String, Object> vertexExistenceParameters;
    private Map<String, Object> edgeExistenceParameters;

    public EdgeExistenceBenchmark(final Connection connection, final Vertex supernode,
                                  final String propertyName, final T[] nodeProperties) {
        super(connection, nodeProperties.length);
        this.supernode = supernode;
        this.propertyName = propertyName;
        this.nodeProperties = nodeProperties;
    }

    @Override
    public void buildUp() {
        vertexExistenceQuery = "g.V()."
                               + "has(searchKey, searchValue)."
                               + "limit(1)."
                               + "toList()";
        indexedEdgeExistenceQuery = "g.V(supernode)."
                                    + "outE('knows')."
                                    + "has(searchKey, adjacentId)."
                                    + "limit(1)."
                                    + "hasNext()";

        vertexExistenceParameters = new HashMap<String, Object>();
        edgeExistenceParameters = new HashMap<String, Object>();
    }

    @Override
    public void performAction(final BenchmarkResult result) throws TimeoutException {
        vertexExistenceParameters.put("searchKey", propertyName);
        edgeExistenceParameters.put("supernode", supernode);

        for (int index = 0; index < stepSize; ++index) {
            vertexExistenceParameters.put("searchValue", nodeProperties[index]);
            ResultSet candidates =
                connection.submitAsync(vertexExistenceQuery, vertexExistenceParameters);
            final Vertex testNode = candidates.one().getVertex();
            if (testNode != null) {
                edgeExistenceParameters.put("adjacentId", testNode.id());
                edgeExistenceParameters.put("searchKey", ImplicitKey.ADJACENT_ID.name());
                connection.submit(indexedEdgeExistenceQuery, edgeExistenceParameters);
            }
        }
    }

    @Override
    public void tearDown() {}
}
