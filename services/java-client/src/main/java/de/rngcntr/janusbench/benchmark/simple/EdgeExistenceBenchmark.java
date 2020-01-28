package de.rngcntr.janusbench.benchmark.simple;

import java.util.List;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import de.rngcntr.janusbench.util.Benchmark;
import de.rngcntr.janusbench.util.BenchmarkResult;
import de.rngcntr.janusbench.util.BenchmarkProperty;

public class EdgeExistenceBenchmark<T> extends Benchmark {

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

    public void setUseEdgeIndex(boolean useEdgeIndex) {
        this.useEdgeIndex = useEdgeIndex;
    }

    @Override
    public void buildUp() {
    }

    @Override
    public void performAction(BenchmarkResult result) {
        BenchmarkProperty useEdgeIndexProperty = new BenchmarkProperty("useEdgeIndex", useEdgeIndex);
        result.injectBenchmarkProperty(useEdgeIndexProperty);
        if (useEdgeIndex) {
            for (int index = 0; index < stepSize; ++index) {
                List<Vertex> candidates = g.V().has(propertyName, nodeProperties[index]).limit(1).toList();
                if (candidates.size() > 0) {
                    Vertex testNode = candidates.get(0);
                    g.V(supernode).
                        outE("knows").
                        has(org.janusgraph.graphdb.types.system.ImplicitKey.ADJACENT_ID.name(), testNode.id()).
                        limit(1).
                        hasNext();
                }
            }
        } else {
            for (int index = 0; index < stepSize; ++index) {
                g.V(supernode).
                    out("knows").
                    has(propertyName, nodeProperties[index]).
                    hasNext();
            }
        }
    }

    @Override
    public void tearDown() {}
}
