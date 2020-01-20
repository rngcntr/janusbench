package de.rngcntr.janusbench.benchmark;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import de.rngcntr.janusbench.benchmark.*;

public class EdgeExistenceBenchmark<T> extends AbstractBenchmark {

    private Vertex supernode;
    private String propertyName;
    private T[] nodeProperties;
    private boolean useEdgeIndex;

    public EdgeExistenceBenchmark(GraphTraversalSource g, Vertex supernode, String propertyName, T[] nodeProperties) {
        super(g, nodeProperties.length);
        this.supernode = supernode;
        this.propertyName = propertyName;
        this.nodeProperties = nodeProperties;
        this.useEdgeIndex = true;
    }

    public void buildUp() {
    }

    public void setUseEdgeIndex(boolean useEdgeIndex) {
        this.useEdgeIndex = useEdgeIndex;
    }

    public void performAction(BenchmarkResult result) {
        result.injectBenchmarkProperty("useEdgeIndex", useEdgeIndex);
        if (useEdgeIndex) {
            for (int index = 0; index < stepSize; ++index) {
                if (g.V().has(propertyName, nodeProperties[index]).hasNext()) {
                    Vertex testNode = g.V().has(propertyName, nodeProperties[index]).next();
                    g.V(supernode).outE().has("inVertexID", testNode.id()).hasNext();
                }
            }
        } else {
            for (int index = 0; index < stepSize; ++index) {
                g.V(supernode).out().has(propertyName, nodeProperties[index]).hasNext();
            }
        }
    }

    public void tearDown() {}
}
