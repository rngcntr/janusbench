public abstract class AbstractBenchmark implements Runnable {
    protected ArrayList<BenchmarkResult> results
    protected GraphTraversalSource g;

    protected int stepSize;
    protected int numThreads;

    public AbstractBenchmark(GraphTraversalSource g) {
        this.results = new ArrayList<BenchmarkResult>();
        this.g = g;
        this.stepSize = 1;
        this.numThreads = 1;
    }

    public AbstractBenchmark(GraphTraversalSource g, int stepSize) {
        this(g);
        this.stepSize = stepSize;
    }

    public void run() {
        BenchmarkResult result = new BenchmarkResult(this);
        buildUp();
        result.vBefore = g.V().count().next();
        result.eBefore = g.E().count().next();
        result.stepSize = stepSize;

        long startTime = System.nanoTime();
        performAction();
        g.tx().commit();
        long stopTime = System.nanoTime();

        result.time = (stopTime - startTime) * 0.000001 / stepSize;
        result.vAfter = g.V().count().next();
        result.eAfter = g.E().count().next();
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

    public class BenchmarkResult {
        private AbstractBenchmark action;
        private int stepSize;

        private int vBefore;
        private int eBefore;
        private int vAfter;
        private int eAfter;

        private double time;

        public BenchmarkResult(AbstractBenchmark action) {
            this.action = action;
        }

        public int getStepSize() {
            return stepSize;
        }

        public int getVBefore() {
            return vBefore;
        }

        public int getEBefore() {
            return eBefore;
        }

        public int getVAfter() {
            return vAfter;
        }

        public int getEAfter() {
            return eAfter;
        }

        public double getTime() {
            return time;
        }

        public String toString() {
            return String.format("RESULT" +
                " action=%s" +
                " stepSize=%d" +
                " v_before=%d" +
                " e_before=%d" +
                " v_after=%d" +
                " e_after=%d" +
                " time=%.3f",
                action.getClass().getName(), stepSize, vBefore, eBefore, vAfter, eAfter, time);
        }
    }
}
