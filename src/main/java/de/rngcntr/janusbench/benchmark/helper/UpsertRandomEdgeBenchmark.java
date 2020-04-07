package de.rngcntr.janusbench.benchmark.helper;

import de.rngcntr.janusbench.backend.Connection;
import de.rngcntr.janusbench.util.Benchmark;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.structure.Vertex;

/**
 * Selects two random vertices from the graph and connects them with an edge.
 * If the connection already exists, only the edge's property is updated.
 */
public class UpsertRandomEdgeBenchmark extends Benchmark {
    private Vertex[] a;
    private Vertex[] b;

    private Random rand;

    private String edgeExistenceQuery;
    private String edgeInsertQuery;
    private String edgeUpdateQuery;
    private Map<String, Object> parameters;

    public UpsertRandomEdgeBenchmark(final Connection connection) { super(connection); }

    public UpsertRandomEdgeBenchmark(final Connection connection, final int stepSize) {
        super(connection, stepSize);
    }

    @Override
    public void buildUp() {
        // prepare edges to insert
        a = new Vertex[stepSize];
        b = new Vertex[stepSize];

        // get a list of all vertices to select from
        final List<Vertex> allVertices = g.V().toList();
        rand = new Random(System.nanoTime());

        for (int i = 0; i < stepSize; ++i) {
            // randomly choose an incoming vertex
            final int selectedIndexA = rand.nextInt(allVertices.size());
            a[i] = allVertices.get(selectedIndexA);

            // one vertex less to sample from
            int selectedIndexB = rand.nextInt(allVertices.size() - 1);
            if (selectedIndexA == selectedIndexB) {
                // if the same index is selected, use another vertex instead
                selectedIndexB = allVertices.size() - 1;
            }

            b[i] = allVertices.get(selectedIndexB);
        }

        edgeExistenceQuery = "g.V(vertexA)"
                             + ".outE(edgeLabel)"
                             + ".as(\"e\")"
                             + ".inV()"
                             + ".is(vertexB)"
                             + ".select(\"e\")"
                             + ".limit(1)"
                             + ".toList()";
        edgeUpdateQuery = "g.E(foundEdge)"
                             + ".property(propertyLabel, propertyValue)"
                             + ".iterate()";
        edgeInsertQuery = "g.addE(edgeLabel)"
                          + ".from(vertexA)"
                          + ".to(vertexB)"
                          + ".property(propertyLabel, propertyValue)"
                          + ".next()";
        parameters = new HashMap<String, Object>();

        parameters.put("edgeLabel", "knows");
        parameters.put("propertyLabel", "lastSeen");
    }

    @Override
    public void performAction() throws TimeoutException {
        for (int index = 0; index < stepSize; ++index) {
            parameters.put("vertexA", a[index]);
            parameters.put("vertexB", b[index]);
            parameters.put("propertyValue", new Date());

            Result candidate = connection.submitAsync(edgeExistenceQuery, parameters).one();
            if (candidate != null) {
                // edge already exists -> update
                parameters.put("foundEdge", candidate.getEdge());
                connection.submit(edgeUpdateQuery, parameters);
            } else {
                // edge does not exist -> insert
                connection.submit(edgeInsertQuery, parameters);
            }
        }
    }

    @Override
    public void tearDown() {}
}
