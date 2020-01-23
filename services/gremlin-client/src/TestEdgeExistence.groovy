ivb = new InsertVerticesBenchmark(g, 2); []
ivb.run(); []

supernode = g.V().next(); []
unconnectedNode = g.V().not(where(is(supernode))).next(); []
unconnectedName = g.V(unconnectedNode).values('name').next(); []

threadNum = 5; []
stepSize = 100; []
innerIterations = 100; []

unconnectedResults = new ArrayList<AbstractBenchmark.BenchmarkResult>(); []

threads = new Thread[threadNum]; []
iterationIdx = 0;[]


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx] = Thread.start {
            isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
            isvb.setCollectStats(false);
            isvb.run();
        }
    }; []

    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx].join();
    }; []
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
eeb.setUseEdgeIndex(false); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx] = Thread.start {
            isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
            isvb.setCollectStats(false);
            isvb.run();
        }
    }; []

    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx].join();
    }; []
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
eeb.setUseEdgeIndex(false); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx] = Thread.start {
            isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
            isvb.setCollectStats(false);
            isvb.run();
        }
    }; []

    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx].join();
    }; []
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
eeb.setUseEdgeIndex(false); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx] = Thread.start {
            isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
            isvb.setCollectStats(false);
            isvb.run();
        }
    }; []

    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx].join();
    }; []
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
eeb.setUseEdgeIndex(false); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx] = Thread.start {
            isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
            isvb.setCollectStats(false);
            isvb.run();
        }
    }; []

    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx].join();
    }; []
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
eeb.setUseEdgeIndex(false); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx] = Thread.start {
            isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
            isvb.setCollectStats(false);
            isvb.run();
        }
    }; []

    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx].join();
    }; []
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
eeb.setUseEdgeIndex(false); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx] = Thread.start {
            isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
            isvb.setCollectStats(false);
            isvb.run();
        }
    }; []

    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx].join();
    }; []
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
eeb.setUseEdgeIndex(false); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx] = Thread.start {
            isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
            isvb.setCollectStats(false);
            isvb.run();
        }
    }; []

    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx].join();
    }; []
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
eeb.setUseEdgeIndex(false); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx] = Thread.start {
            isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
            isvb.setCollectStats(false);
            isvb.run();
        }
    }; []

    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx].join();
    }; []
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
eeb.setUseEdgeIndex(false); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx] = Thread.start {
            isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
            isvb.setCollectStats(false);
            isvb.run();
        }
    }; []

    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx].join();
    }; []
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
eeb.setUseEdgeIndex(false); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx] = Thread.start {
            isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
            isvb.setCollectStats(false);
            isvb.run();
        }
    }; []

    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx].join();
    }; []
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
eeb.setUseEdgeIndex(false); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx] = Thread.start {
            isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
            isvb.setCollectStats(false);
            isvb.run();
        }
    }; []

    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx].join();
    }; []
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
eeb.setUseEdgeIndex(false); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx] = Thread.start {
            isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
            isvb.setCollectStats(false);
            isvb.run();
        }
    }; []

    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx].join();
    }; []
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
eeb.setUseEdgeIndex(false); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx] = Thread.start {
            isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
            isvb.setCollectStats(false);
            isvb.run();
        }
    }; []

    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx].join();
    }; []
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
eeb.setUseEdgeIndex(false); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx] = Thread.start {
            isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
            isvb.setCollectStats(false);
            isvb.run();
        }
    }; []

    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx].join();
    }; []
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
eeb.setUseEdgeIndex(false); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx] = Thread.start {
            isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
            isvb.setCollectStats(false);
            isvb.run();
        }
    }; []

    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx].join();
    }; []
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
eeb.setUseEdgeIndex(false); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx] = Thread.start {
            isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
            isvb.setCollectStats(false);
            isvb.run();
        }
    }; []

    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx].join();
    }; []
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
eeb.setUseEdgeIndex(false); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx] = Thread.start {
            isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
            isvb.setCollectStats(false);
            isvb.run();
        }
    }; []

    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx].join();
    }; []
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
eeb.setUseEdgeIndex(false); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx] = Thread.start {
            isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
            isvb.setCollectStats(false);
            isvb.run();
        }
    }; []

    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx].join();
    }; []
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
eeb.setUseEdgeIndex(false); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx] = Thread.start {
            isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
            isvb.setCollectStats(false);
            isvb.run();
        }
    }; []

    for (threadIdx = 0; threadIdx < threadNum; ++threadIdx) {
        threads[threadIdx].join();
    }; []
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
eeb.setUseEdgeIndex(false); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()
