:load /data/AbstractBenchmark.groovy
:load /data/InsertVerticesBenchmark.groovy
:load /data/UpsertSupernodeVerticesBenchmark.groovy
:load /data/UpsertRandomEdgeBenchmark.groovy
:load /data/UpsertSupernodeEdgeBenchmark.groovy

ivb = new InsertVerticesBenchmark(g)
ivb.run(1)
supernode = g.V().next()

usvb = new UpsertSupernodeVerticesBenchmark(g, supernode)
usvb.runUntil(100, {r -> r.getVAfter() >= 2000})
