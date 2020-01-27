public class ComposedBenchmark extends AbstractBenchmark {

    private ArrayList<AbstractBenchmark> components;

    public ComposedBenchmark (GraphTraversalSource g) {
        super(g);
        components = new ArrayList<AbstractBenchmark>();
    }

    public ComposedBenchmark (GraphTraversalSource g, int stepSize) {
        super(g, stepSize);
        components = new ArrayList<AbstractBenchmark>();
    }

    public void addComponent (AbstractBenchmark component) {
        if (component != this) {
            components.add(component);
        }
    }

    public void addComponent (AbstractBenchmark component, int repetitions) {
        if (component != this) {
            for (int repCnt = 0; repCnt < repetitions; ++repCnt) {
                components.add(component);
            }
        }
    }

    public void buildUp() {}

    public void performAction(BenchmarkResult result) {
        for (int step = 0; step < stepSize; ++step) {
            for (AbstractBenchmark component : components) {
                component.run();
                results.addAll(component.getResults());
                component.resetResults();
            }
        }
    }

    public void tearDown() {}
}
