package de.rngcntr.janusbench.util;

import de.rngcntr.janusbench.backend.Connection;
import de.rngcntr.janusbench.backend.configuration.Configuration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;

/**
 * A Benchmark is a unit of code whose execution time is measured.
 * Subclasses of Benchmark contain graph traversals which are run against a connection to a graph
 * backend.<br>
 * The lifecycle of a Benchmark consists of three main procedures:
 * <ol>
 * <li><code>buildUp()</code></li>
 * <li><code>performAction()</code> (measured)</li>
 * <li><code>tearDown()</code></li>
 * </ol>
 *
 * @author Florian Grieskamp
 */
public abstract class Benchmark implements Runnable {
    protected ArrayList<BenchmarkResult> results;
    protected Connection connection;
    protected GraphTraversalSource g;

    protected int stepSize;

    private final ArrayList<BenchmarkProperty> trackBeforeRun;
    private final ArrayList<BenchmarkProperty> trackAfterRun;

    private boolean collectResults;

    protected Configuration configuration;

    private String displayName;

    /**
     * Initializes a Benchmark with a step size of 1.
     * This Benchmark does not have connection and therefore is unable to run until {@link
     * #setConnection(Connection)} is called.
     */
    public Benchmark() {
        this.stepSize = 1;
        this.results = new ArrayList<BenchmarkResult>();
        this.trackBeforeRun = new ArrayList<BenchmarkProperty>();
        this.trackAfterRun = new ArrayList<BenchmarkProperty>();
        this.collectResults = true;
        this.displayName = null;
    }

    /**
     * Initializes a Benchmark using only a connection. The step size will be set to 1.
     * 
     * @param connection The connection to the graph backend.
     */
    public Benchmark(final Connection connection) {
        this();
        setConnection(connection);
    }

    /**
     * Initializes a Benchmark using a connection and step size.
     * 
     * @param connection The connection to the graph backend.
     * @param stepSize The size of a single benchmark run.
     */
    public Benchmark(final Connection connection, final int stepSize) {
        this(connection);
        this.stepSize = stepSize;
    }

    /**
     * Redirects the connection after initialization.
     * 
     * @param connection The new connection to use.
     */
    protected void setConnection(final Connection connection) {
        this.connection = connection;
        this.g = connection.g();
    }

    /**
     * Sets the configuration which is used to run this benchmark. This configuration is later used
     * to log the used storage and index backends.
     * 
     * @param configuration The configuration from which the used backends are obtained.
     */
    public void setConfiguration(final Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Enables or disables the collection of results.
     * This can be helpful if a benchmark is used only as a set-up for following benchmarks.
     *
     * @param collectResults <ul><li>true if the results shall be collected</li><li>false if the
     *     results shall not be collected</li></ul>
     */
    public void setCollectResults(final boolean collectResults) {
        this.collectResults = collectResults;
    }

    /**
     * Sets the content of the action property of all benchmark results produced by this benchmark.
     *
     * @param displayName The new name for this benchmark.
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * The content of the action property of all benchmark results produced by this benchmark.
     *
     * @return The name of this benchmark. If not modified explicitly, this is the benchmark's class
     *     name.
     */
    public String getDisplayName() {
        if (displayName != null) {
            return displayName;
        } else {
            return getClass().getSimpleName();
        }
    }

    /**
     * Runs a single iteration of the benchmark.
     * This includes calling the {@link #buildUp()} and {@link #tearDown()} methods, measuring the
     * execution time and evaluating BenchmarkProperties.
     */
    public void run() {
        buildUp();

        final BenchmarkResult result = new BenchmarkResult(this);
        result.setConfiguration(configuration);
        final BenchmarkProperty stepSizeProperty = new BenchmarkProperty("stepSize", () -> stepSize);
        result.injectBenchmarkProperty(stepSizeProperty);

        for (final BenchmarkProperty beforeProperty : trackBeforeRun) {
            result.injectBenchmarkProperty(beforeProperty);
        }

        final long startTime = System.nanoTime();

        boolean successful = false;

        while (!successful) {
            try {
                performAction();
                connection.submit("g.tx().commit()");
                successful = true;
            } catch (TimeoutException tex) {
                connection.submit("g.tx().rollback()");
            }
        }

        final long stopTime = System.nanoTime();

        for (final BenchmarkProperty afterProperty : trackAfterRun) {
            result.injectBenchmarkProperty(afterProperty);
        }

        final BenchmarkProperty timeProperty =
            new BenchmarkProperty("time", () -> (stopTime - startTime) / 1000000.0);
        result.injectBenchmarkProperty(timeProperty);

        tearDown();

        if (collectResults) {
            results.add(result);
            ResultLogger.getInstance().log(result);
        }
    }

    /**
     * Runs the Benchmark until a break condition is satisfied.
     * The Benchmark is executed at least once and is then repeated until the break condition
     * evaluates to true.
     * The break condition is evaluated after each run of the benchmark.<br><br>
     *
     * Example: <code>runUntil(r -&gt; r.getBenchmarkProperty("time") &gt; 1000)</code> will repeat the
     * benchmark until the latest run took longer than 1000ms.
     *
     * @param breakCondition The break condition which causes the benchmark to stop if it returns
     *     true.
     */
    public void runUntil(final IBreakCondition breakCondition) {
        // check break condition on latest result
        do {
            run();
        } while (!breakCondition.stop(results.get(results.size() - 1)));
    }

    /**
     * Use this method to implement preparations that should be run before the benchmark.
     */
    public abstract void buildUp();

    /**
     * Use this method to implement the time-measured code of the benchmark.
     *
     * @throws TimeoutException to indicate that the benchmark did not succeed.
     */
    public abstract void performAction() throws TimeoutException;

    /**
     * Use this method to implement a tear down that should be run after the benchmark.
     */
    public abstract void tearDown();

    /**
     * Returns all results recorded by this benchmark.
     * 
     * @return An ArrayList of all results.
     */
    public ArrayList<BenchmarkResult> getResults() { return results; }

    /**
     * Returns only those results that match a specific subclass of Benchmark.
     * 
     * @param cls The subclass to match.
     * @return All recorded results whose "action" Property matches cls.
     */
    public List<BenchmarkResult> getResults(final Class<? extends Benchmark> cls) {
        return results.stream()
            .filter(r -> r.getBenchmarkProperty("action").equals(cls.getSimpleName()))
            .collect(Collectors.toList());
    }

    /**
     * Clears all recorded results.
     */
    public void resetResults() { results.clear(); }

    /**
     * Sets the size of each performed operation.
     * Using the step size, Benchmark implementations can determine the batch size of a single run.
     * 
     * @param stepSize The integer size of operations (repetitions) per run.
     */
    public void setStepSize(final int stepSize) { this.stepSize = stepSize; }

    /**
     * Adds a new BenchmarkProperty to the Benchmark.
     * This BenchmarkProperty is later evaluated and contained in the produced {@link
     * BenchmarkResult}
     *
     * @param property The BenchmarkProperty which describes the Benchmark or it's result.
     * @param tracking Whether to evaluate this BenchmarkProperty before or after the benchmark run.
     */
    public void collectBenchmarkProperty(final BenchmarkProperty property,
                                         final BenchmarkProperty.Tracking tracking) {
        switch (tracking) {
        case BEFORE:
            trackBeforeRun.add(property);
            break;
        case AFTER:
            trackAfterRun.add(property);
            break;
        }
    }

    /**
     * A functional interface which can be used as a condition to stop further execution of a benchmark.
     * An IBreakCondition maps a BenchmarkResult to a boolean which determines whether or not to stop the Benchmark execution.
     * 
     * @see #runUntil(IBreakCondition)
     */
    public interface IBreakCondition { boolean stop(BenchmarkResult result); }
}
