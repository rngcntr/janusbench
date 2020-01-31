:load /data/benchmark.groovy

ivb = new InsertVerticesBenchmark(g, 1)
ivb.run()

supernode = g.V().next()

// create inserter
isvb = new InsertSupernodeVerticesBenchmark(g, 500, supernode)

connectionsBefore = new AbstractBenchmark.BenchmarkProperty("vBefore", g.V(supernode).outE().count())
isvb.collectBenchmarkProperty(connectionsBefore, AbstractBenchmark.BenchmarkProperty.BEFORE);

connectionsAfter = new AbstractBenchmark.BenchmarkProperty("vAfter", g.V(supernode).outE().count())
isvb.collectBenchmarkProperty(connectionsAfter, AbstractBenchmark.BenchmarkProperty.AFTER);

// create edge existence checker
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ supernode.values('name') ])
eeb.setUseEdgeIndex(true)

connections = new AbstractBenchmark.BenchmarkProperty("connections", g.V(supernode).outE().count())
eeb.collectBenchmarkProperty(connections, AbstractBenchmark.BenchmarkProperty.AFTER);

// create composed benchmark
cb = new ComposedBenchmark(g, 10)

cb.addComponent(isvb, 10)
cb.addComponent(eeb)

cb.run()
cb.getResults(EdgeExistenceBenchmark.class)
