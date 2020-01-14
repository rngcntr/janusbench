public abstract class AbstractBenchmark implements Runnable {
    protected ArrayList<BenchmarkResult> results
    protected GraphTraversalSource g;

    protected int stepSize;
    protected int numThreads;

    private boolean collectStats;

    public AbstractBenchmark(GraphTraversalSource g) {
        this.results = new ArrayList<BenchmarkResult>();
        this.g = g;
        this.stepSize = 1;
        this.numThreads = 1;
        this.collectStats = true;
    }

    public AbstractBenchmark(GraphTraversalSource g, int stepSize) {
        this(g);
        this.stepSize = stepSize;
    }

    public void run() {
        BenchmarkResult result = new BenchmarkResult(this);
        buildUp();

        if (collectStats) {
            result.statsBefore = new GraphStatistics(g);
        }

        result.stepSize = stepSize;

        long startTime = System.nanoTime();
        performAction();
        g.tx().commit();
        long stopTime = System.nanoTime();

        result.time = (stopTime - startTime) * 0.000001 / stepSize;

        if (collectStats) {
            result.statsBefore = new GraphStatistics(g);
        }

        tearDown();

        results.add(result);
    }

    public void runUntil(Closure breakCondition) {
        run(stepSize);

        // check break condition on latest result
        while (breakCondition(results.get(results.size() - 1)) == false) {
            run(stepSize);
        }
    }

    public abstract void buildUp();
    public abstract void performAction();
    public abstract void tearDown();

    public ArrayList<BenchmarkResult> getResults() {
        return results;
    }

    public void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }

    public void setCollectStats(boolean collectStats) {
        this.collectStats = collectStats;
    }

    public class BenchmarkResult {
        private AbstractBenchmark action;
        private int stepSize;

        private GraphStatistics statsBefore;
        private GraphStatistics statsAfter;

        private double time;

        public BenchmarkResult(AbstractBenchmark action) {
            this.action = action;
        }

        public int getStepSize() {
            return stepSize;
        }

        public int getStatsBefore() {
            return statsBefore;
        }

        public int getStatsAfter() {
            return statsAfter;
        }

        public double getTime() {
            return time;
        }

        public String toString() {
            if (statsBefore != null && statsAfter != null) {
                return String.format("RESULT" +
                    " action=%s" +
                    " stepSize=%d" +
                    " v_before=%d" +
                    " e_before=%d" +
                    " v_after=%d" +
                    " e_after=%d" +
                    " time=%.3f",
                    action.getClass().getName(),
                    stepSize,
                    statsBefore.getNumVertices(),
                    statsBefore.getNumEdges(),
                    statsAfter.getNumVertices(),
                    statsAfter.getNumEdges(),
                    time);
            } else {
                return String.format("RESULT" +
                    " action=%s" +
                    " stepSize=%d" +
                    " time=%.3f",
                    action.getClass().getName(),
                    stepSize,
                    time);
            }
        }
    }

    public class GraphStatistics {
        private int numVertices;
        private int numEdges;

        public GraphStatistics(GraphTraversalSource g) {
            this.numVertices = g.V().count().next();
            this.numEdges = g.E().count().next();
        }

        public int getNumVertices() {
            return numVertices;
        }

        public int getNumEdges() {
            return numEdges;
        }
    }
}
