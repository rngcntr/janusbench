package de.rngcntr.janusbench.benchmark.helper;

import de.rngcntr.janusbench.backend.Connection;
import de.rngcntr.janusbench.util.Benchmark;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Inserts random vertices with name and age properties into the graph.
 */
public class InsertVerticesBenchmark extends Benchmark {

    private String[] names;
    private int nameLength;

    private int[] ages;
    private int minAge;
    private int maxAge;

    private Random rand;

    private String insertQuery;
    private Map<String, Object> insertParameters;

    public InsertVerticesBenchmark() { super(); }

    public InsertVerticesBenchmark(final Connection connection) { super(connection); }

    public InsertVerticesBenchmark(final Connection connection, final int stepSize) {
        super(connection, stepSize);
    }

    @Override
    public void buildUp() {
        names = new String[stepSize];
        nameLength = 8;

        ages = new int[stepSize];
        minAge = 18;
        maxAge = 100;

        rand = new Random(System.nanoTime());

        for (int i = 0; i < stepSize; ++i) {
            names[i] = RandomStringUtils.randomAlphanumeric(nameLength);
            ages[i] = rand.nextInt(maxAge - minAge) + minAge;
        }

        insertQuery = "g.addV(vertexLabel)."
                      + "property(nameIdentifier, nameValue)."
                      + "property(ageIdentifier, ageValue)."
                      + "iterate()";
        insertParameters = new HashMap<String, Object>();

        insertParameters.put("vertexLabel", "person");
        insertParameters.put("nameIdentifier", "name");
        insertParameters.put("ageIdentifier", "age");
    }

    @Override
    public void performAction() throws TimeoutException {
        for (int index = 0; index < stepSize; ++index) {
            // assume vertex does not exist -> insert
            insertParameters.put("nameValue", names[index]);
            insertParameters.put("ageValue", ages[index]);
            connection.submit(insertQuery, insertParameters);
        }
    }

    @Override
    public void tearDown() {}

    public String[] getNames() { return names; }
}
