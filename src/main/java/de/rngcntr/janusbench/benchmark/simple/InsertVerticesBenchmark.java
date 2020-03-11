package de.rngcntr.janusbench.benchmark.simple;

import de.rngcntr.janusbench.backend.Connection;
import de.rngcntr.janusbench.util.Benchmark;
import de.rngcntr.janusbench.util.BenchmarkResult;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import org.apache.commons.lang3.RandomStringUtils;

public class InsertVerticesBenchmark extends Benchmark {

    private String[] names;
    private int nameLength;

    private int[] ages;
    private int minAge;
    private int maxAge;

    private Random rand;

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
    }

    @Override
    public void performAction() throws TimeoutException {
        for (int index = 0; index < stepSize; ++index) {
            // assume vertex does not exist -> insert
            g.addV("person").property("name", names[index]).property("age", ages[index]).next();
        }
    }

    @Override
    public void tearDown() {}

    public String[] getNames() { return names; }
}
