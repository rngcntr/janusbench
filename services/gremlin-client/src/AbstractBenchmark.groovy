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
            result.injectBenchmarkProperty("vBefore", g.V().count().next());
            result.injectBenchmarkProperty("eBefore", g.E().count().next());
        }

        result.injectBenchmarkProperty("stepSize", stepSize);

        long startTime = System.nanoTime();
        performAction(result);

        boolean committed = false;
        while (!committed) {
            try {
                g.tx().commit();
                committed = true;
            } catch (Exception ex) {}
        }

        long stopTime = System.nanoTime();

        result.injectBenchmarkProperty("time", (stopTime - startTime) * 0.000001 / stepSize);

        if (collectStats) {
            result.injectBenchmarkProperty("vAfter", g.V().count().next());
            result.injectBenchmarkProperty("eAfter", g.E().count().next());
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
    public abstract void performAction(BenchmarkResult result);
    public abstract void tearDown();

    public ArrayList<BenchmarkResult> getResults() {
        return results;
    }

    public void resetResults() {
        results.clear();
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
        private HashMap<String, Object> benchmarkProperties;

        public BenchmarkResult(AbstractBenchmark action) {
            this.benchmarkProperties = new HashMap<String, Object>();
            injectBenchmarkProperty("action", action.getClass().getName());
        }

        public void injectBenchmarkProperty (String name, Object value) {
            benchmarkProperties.put(name, value);
        }

        public Object getBenchmarkProperty (String name) {
            return benchmarkProperties.get(name);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("RESULT");
            for (Map.Entry property : benchmarkProperties.entrySet()) {
                sb.append(" ");
                sb.append(property.getKey().toString());
                sb.append("=");
                sb.append(property.getValue().toString());
            }

            return sb.toString();
        }
    }
}
