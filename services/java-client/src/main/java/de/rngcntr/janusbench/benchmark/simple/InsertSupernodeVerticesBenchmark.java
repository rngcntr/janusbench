package de.rngcntr.janusbench.benchmark.simple;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import de.rngcntr.janusbench.tinkerpop.Connection;
import de.rngcntr.janusbench.util.Benchmark;
import de.rngcntr.janusbench.util.BenchmarkResult;

public class InsertSupernodeVerticesBenchmark extends Benchmark {
    private Vertex supernode;

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

    public InsertSupernodeVerticesBenchmark(Connection connection, int stepSize, Vertex supernode) {
        super(connection, stepSize);
        this.supernode = supernode;
    }

    public InsertSupernodeVerticesBenchmark(Connection connection, Vertex supernode) {
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
    public void performAction(BenchmarkResult result) {
        for (int index = 0; index < stepSize; ++index) {
            // assume vertex does not exist -> insert
            vertexParameters.put("nameValue", names[index]);
            vertexParameters.put("ageValue", ages[index]);

            insertedVertices[index] = connection.submitAsync(vertexInsertQuery, vertexParameters);
        }
        
        for (int index = 0; index < stepSize; ++index) {
            edgeParameters.put("supernode", supernode);
            edgeParameters.put("lastSeenValue", dates[index]);
            edgeParameters.put("timesSeenValue", timesSeen[index]);
            edgeParameters.put("insertedVertex", insertedVertices[index].one().getVertex());

            insertedEdges[index] = connection.submitAsync(edgeInsertQuery, edgeParameters);
        }

        for (int index = 0; index < stepSize; ++index) {
            connection.awaitResults(insertedEdges[index]);
        }
    }

    @Override
    public void tearDown() {}

    public String[] getNames() {
        return names;
    }
}