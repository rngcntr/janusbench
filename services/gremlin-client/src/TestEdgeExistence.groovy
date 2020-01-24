ivb = new InsertVerticesBenchmark(g, 2); []
ivb.run(); []

supernode = g.V().next(); []
unconnectedNode = g.V().not(where(is(supernode))).next(); []
unconnectedName = g.V(unconnectedNode).values('name').next(); []

stepSize = 500; []
innerIterations = 100; []

unconnectedResults = new ArrayList<AbstractBenchmark.BenchmarkResult>(); []

iterationIdx = 0;[]


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
    isvb.setCollectStats(false);
    isvb.run();
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
    isvb.setCollectStats(false);
    isvb.run();
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
    isvb.setCollectStats(false);
    isvb.run();
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
    isvb.setCollectStats(false);
    isvb.run();
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
    isvb.setCollectStats(false);
    isvb.run();
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
    isvb.setCollectStats(false);
    isvb.run();
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
    isvb.setCollectStats(false);
    isvb.run();
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
    isvb.setCollectStats(false);
    isvb.run();
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
    isvb.setCollectStats(false);
    isvb.run();
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
    isvb.setCollectStats(false);
    isvb.run();
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
    isvb.setCollectStats(false);
    isvb.run();
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
    isvb.setCollectStats(false);
    isvb.run();
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
    isvb.setCollectStats(false);
    isvb.run();
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
    isvb.setCollectStats(false);
    isvb.run();
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
    isvb.setCollectStats(false);
    isvb.run();
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
    isvb.setCollectStats(false);
    isvb.run();
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
    isvb.setCollectStats(false);
    isvb.run();
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
    isvb.setCollectStats(false);
    isvb.run();
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
    isvb.setCollectStats(false);
    isvb.run();
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()


for (innerIterationIdx = 0; innerIterationIdx < innerIterations; ++innerIterationIdx) {
    isvb = new InsertSupernodeVerticesBenchmark(g, stepSize, supernode);
    isvb.setCollectStats(false);
    isvb.run();
}

++iterationIdx; []
eeb = new EdgeExistenceBenchmark<String>(g, supernode, 'name', (String[]) [ unconnectedName ]); []
eeb.setCollectStats(false); []
eeb.setUseEdgeIndex(true); []
try{ eeb.run(); } catch (Exception e) {}; []
for (AbstractBenchmark.BenchmarkResult r : eeb.getResults()) {
    r.injectBenchmarkProperty("connectedVertices", g.V().count().next() - 2);
    unconnectedResults.add(r);
}; []
eeb.getResults()
