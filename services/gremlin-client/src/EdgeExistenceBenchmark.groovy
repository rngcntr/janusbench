public class EdgeExistenceBenchmark<T> extends AbstractBenchmark {

    private Vertex supernode;
    private String propertyName;
    private T[] nodeProperties;

    public EdgeExistenceBenchmark(GraphTraversalSource g, Vertex supernode, String propertyName, T[] nodeProperties) {
        super(g, nodeProperties.length);
        this.supernode = supernode;
        this.propertyName = propertyName;
        this.nodeProperties = nodeProperties;
    }

    public void buildUp() {
    }

    public void performAction() {
        for (int index = 0; index < stepSize; ++index) {
            g.V(supernode).out().has(propertyName, nodeProperties[index]).hasNext();
        }
    }

    public void tearDown() {}
}
