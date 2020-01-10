public abstract class AbstractBenchmark {
    protected BenchmarkResult result
    protected GraphTraversalSource g;

    public AbstractBenchmark(GraphTraversalSource g) {
        result = null;
        this.g = g;
    }

    public BenchmarkResult run(int amount) {
        result = new BenchmarkResult(this);
        buildUp(amount);
        result.vBefore = g.V().count().next();
        result.eBefore = g.E().count().next();
        result.amount = amount;

        long startTime = System.nanoTime();
        performAction(amount);
        g.tx().commit();
        long stopTime = System.nanoTime();

        result.time = (stopTime - startTime) * 0.000001 / amount;
        result.vAfter = g.V().count().next();
        result.eAfter = g.E().count().next();
        tearDown(amount);
        return result;
    }

    public ArrayList<BenchmarkResult> runUntil(int amount, Closure breakCondition) {
        ArrayList<BenchmarkResult> results = new ArrayList<BenchmarkResult>();
        BenchmarkResult latestResult;
        latestResult = run(amount);
        results.add(latestResult);

        while (breakCondition(latestResult) == false) {
            latestResult = run(amount);
            results.add(latestResult);
        }

        return results;
    }

    public abstract void buildUp(int amount);
    public abstract void performAction(int amount);
    public abstract void tearDown(int amount);

    public BenchmarkResult getResult() {
        return result;
    }

    public class BenchmarkResult {
        private AbstractBenchmark action;
        private int amount;

        private int vBefore;
        private int eBefore;
        private int vAfter;
        private int eAfter;

        private double time;

        public BenchmarkResult(AbstractBenchmark action) {
            this.action = action;

        }

        public int getAmount() {
            return amount;
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
                " amount=%d" +
                " v_before=%d" +
                " e_before=%d" +
                " v_after=%d" +
                " e_after=%d" +
                " time=%.3f",
                action.getClass().getName(), amount, vBefore, eBefore, vAfter, eAfter, time);
        }
    }
}
