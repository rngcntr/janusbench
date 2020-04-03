package de.rngcntr.janusbench.benchmark;

import de.rngcntr.janusbench.util.ComposableBenchmark;
import de.rngcntr.janusbench.util.MicroBenchmark;
import java.util.HashMap;

public class MicroBenchmarkRunner extends ComposableBenchmark {

    public int runs;
    public String action;
    public HashMap<String, Object> parameters = null;

    private MicroBenchmark mb;

    public MicroBenchmarkRunner() { super(); }

    @Override
    public void buildUp() {
        mb = new MicroBenchmark(connection, action, parameters);

        // propagate original display name to underlying micro benchmark;
        mb.setDisplayName(getDisplayName());
        this.setDisplayName(String.format("%s:%s", getClass().getSimpleName(), getDisplayName()));

        addComponent(mb, runs);
    }
}