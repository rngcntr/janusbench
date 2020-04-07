package de.rngcntr.janusbench.benchmark.helper;

import de.rngcntr.janusbench.backend.Connection;
import de.rngcntr.janusbench.util.Benchmark;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.apache.tinkerpop.gremlin.structure.Vertex;

/**
 * Inserts random vertices with name and age properties into the graph and connects all of them to a
 * single supernode.
 */
public class InsertSupernodeVerticesBenchmark extends Benchmark {
    private final Vertex supernode;

    private String[] names;
    private int nameLength;

    private int[] ages;
    private int minAge;
    private int maxAge;

    private Random rand;

    private Date[] dates;

    private int[] timesSeen;

    private String vertexInsertQuery;
    private String edgeInsertQuery;

    private Map<String, Object> vertexParameters;
    private Map<String, Object> edgeParameters;

    private ResultSet[] insertedVertices;
    private ResultSet[] insertedEdges;

    private final int BATCH_SIZE = 100;

    public InsertSupernodeVerticesBenchmark(final Connection connection, final int stepSize,
                                            final Vertex supernode) {
        super(connection, stepSize);
        this.supernode = supernode;
    }

    public InsertSupernodeVerticesBenchmark(final Connection connection, final Vertex supernode) {
        super(connection);
        this.supernode = supernode;
    }

    @Override
    public void buildUp() {
        names = new String[stepSize];
        nameLength = 8;

        ages = new int[stepSize];
        minAge = 18;
        maxAge = 100;

        dates = new Date[stepSize];

        timesSeen = new int[stepSize];

        rand = new Random(System.nanoTime());

        for (int i = 0; i < stepSize; ++i) {
            names[i] = RandomStringUtils.randomAlphanumeric(nameLength);
            ages[i] = rand.nextInt(maxAge - minAge) + minAge;
            dates[i] = new Date();
            timesSeen[i] = rand.nextInt();
        }

        vertexInsertQuery = "g.addV('person')."
                            + "property('name', nameValue)."
                            + "property('age', ageValue)."
                            + "next()";
        edgeInsertQuery = "g.addE('knows')."
                          + "from(supernode)."
                          + "to(insertedVertex)."
                          + "property('lastSeen', lastSeenValue)."
                          + "property('timesSeen', timesSeenValue)."
                          + "next()";
        vertexParameters = new HashMap<String, Object>();
        edgeParameters = new HashMap<String, Object>();

        insertedVertices = new ResultSet[stepSize];
        insertedEdges = new ResultSet[stepSize];
    }

    @Override
    public void performAction() throws TimeoutException {
        for (int startIndex = 0; startIndex < stepSize; startIndex += BATCH_SIZE) {
            for (int index = startIndex; index - startIndex < BATCH_SIZE && index < stepSize; ++index) {
                // assume vertex does not exist -> insert
                vertexParameters.put("nameValue", names[index]);
                vertexParameters.put("ageValue", ages[index]);

                insertedVertices[index] =
                    connection.submitAsync(vertexInsertQuery, vertexParameters);
            }

            for (int index = startIndex; index - startIndex < BATCH_SIZE && index < stepSize; ++index) {
                edgeParameters.put("supernode", supernode);
                edgeParameters.put("lastSeenValue", dates[index]);
                edgeParameters.put("timesSeenValue", timesSeen[index]);
                edgeParameters.put("insertedVertex", insertedVertices[index].one().getVertex());

                insertedEdges[index] = connection.submitAsync(edgeInsertQuery, edgeParameters);
            }

            for (int index = startIndex; index - startIndex < BATCH_SIZE && index < stepSize; ++index) {
                connection.awaitResults(insertedEdges[index]);
            }
        }
    }

    @Override
    public void tearDown() {}

    public String[] getNames() { return names; }
}
