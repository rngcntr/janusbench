package de.rngcntr.janusbench.benchmark;

import de.rngcntr.janusbench.util.BenchmarkProperty;
import de.rngcntr.janusbench.util.BuildUpQuery;
import de.rngcntr.janusbench.util.ComposableBenchmark;
import de.rngcntr.janusbench.util.MicroBenchmark;
import de.rngcntr.janusbench.util.BenchmarkProperty.Tracking;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class MicroBenchmarkRunner extends ComposableBenchmark {

    public int runs;
    public String action;
    public List<BuildUpQuery> buildUp = null;
    public HashMap<String, Object> parameters = new HashMap<>();

    public HashMap<String, String> propertiesBefore = new HashMap<>();
    public HashMap<String, String> propertiesAfter = new HashMap<>();

    private MicroBenchmark mb;

    public MicroBenchmarkRunner() { super(); }

    @Override
    public void buildUp() {
        mb = new MicroBenchmark(connection, action, parameters);

        // perform initialization
        mb.setBuildUp(buildUp);

        // set benchmark properties to collect
        for (Entry<String, String> p : propertiesBefore.entrySet()) {
            BenchmarkProperty benchmarkProperty = new BenchmarkProperty(p.getKey(), p.getValue());
            mb.collectBenchmarkProperty(benchmarkProperty, Tracking.BEFORE);
        }

        for (Entry<String, String> p : propertiesAfter.entrySet()) {
            BenchmarkProperty benchmarkProperty = new BenchmarkProperty(p.getKey(), p.getValue());
            mb.collectBenchmarkProperty(benchmarkProperty, Tracking.AFTER);
        }

        // propagate original display name to underlying micro benchmark;
        mb.setDisplayName(getDisplayName());
        this.setDisplayName(String.format("%s.%s", getClass().getSimpleName(), getDisplayName()));

        addComponent(mb, runs);
    }

}