:load /data/benchmark.groovy

ivb = new InsertVerticesBenchmark(g, 2)
ivb.run()

supernode = g.V().next()
unconnectedNode = g.V().not(where(is(supernode))).next()

cb = new ComposedBenchmark(g)
cb.setCollectStats(false)

isvb = new InsertSupernodeVerticesBenchmark(g, 500, supernode)
isvb.setCollectStats(false)

eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedNode.values('name') ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []

cb.addComponent(isvb, 100)
cb.addComponent(eeb)

cb.run()
