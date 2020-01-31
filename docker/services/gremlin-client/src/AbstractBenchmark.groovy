public abstract class AbstractBenchmark implements Runnable {
    protected ArrayList<BenchmarkResult> results
    protected GraphTraversalSource g;

    protected int stepSize;

    private ArrayList<BenchmarkProperty> trackBeforeRun;
    private ArrayList<BenchmarkProperty> trackAfterRun;

    public AbstractBenchmark(GraphTraversalSource g) {
        this.g = g;
        this.stepSize = 1;
        this.results = new ArrayList<BenchmarkResult>();
        this.trackBeforeRun = new ArrayList<BenchmarkProperty>();
        this.trackAfterRun = new ArrayList<BenchmarkProperty>();
    }

    public AbstractBenchmark(GraphTraversalSource g, int stepSize) {
        this(g);
        this.stepSize = stepSize;
    }

    public void run() {
        BenchmarkResult result = new BenchmarkResult(this);
        buildUp();

        BenchmarkProperty stepSizeProperty = new BenchmarkProperty("stepSize", stepSize);
        result.injectBenchmarkProperty(stepSizeProperty);

        for (BenchmarkProperty beforeProperty : trackBeforeRun) {
            result.injectBenchmarkProperty(beforeProperty);
        }

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

        for (BenchmarkProperty afterProperty : trackAfterRun) {
            result.injectBenchmarkProperty(afterProperty);
        }

        BenchmarkProperty timeProperty = new BenchmarkProperty("time", (stopTime - startTime) / 1000000.0 / stepSize);
        result.injectBenchmarkProperty(timeProperty);

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

    public ArrayList<BenchmarkResult> getResults(Class cls) {
        return results.findAll{
            r -> r.getBenchmarkProperty("action").equals(cls.getSimpleName())
        }
    }

    public void resetResults() {
        results.clear();
    }

    public void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }

    public void collectBenchmarkProperty (BenchmarkProperty property, BenchmarkProperty.Tracking tracking) {
        switch (tracking) {
            case BenchmarkProperty.BEFORE:
                trackBeforeRun.add(property);
                break;
            case BenchmarkProperty.AFTER:
                trackAfterRun.add(property);
                break;
        }
    }

    public static class BenchmarkProperty {

        private enum Tracking {
            BEFORE, AFTER;
        }

        public static final Tracking BEFORE = Tracking.BEFORE;
        public static final Tracking AFTER = Tracking.AFTER;

        private String name;
        private Object value;

        public BenchmarkProperty (String name, org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.DefaultGraphTraversal statCollector) {
            this.name = name;
            this.value = statCollector;
        }

        public BenchmarkProperty (String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public String getName () {
            return name;
        }

        public Object evaluate () {
            if (value instanceof org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.DefaultGraphTraversal) {
                org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.DefaultGraphTraversal statCollector = (org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.DefaultGraphTraversal) value.clone();
                return statCollector.next();
            } else {
                return value;
            }
        }
    }

    public static class BenchmarkResult {
        private HashMap<String, Object> benchmarkProperties;

        public BenchmarkResult(AbstractBenchmark action) {
            this.benchmarkProperties = new HashMap<String, Object>();
            BenchmarkProperty actionProperty = new BenchmarkProperty("action", action.getClass().getSimpleName());
            injectBenchmarkProperty(actionProperty);
        }

        public void injectBenchmarkProperty (BenchmarkProperty property) {
            benchmarkProperties.put(property.getName(), property.evaluate());
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
