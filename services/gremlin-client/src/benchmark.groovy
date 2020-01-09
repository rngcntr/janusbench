:load /data/insertVertices.groovy
:load /data/upsertEdges.groovy

def randomBenchmark(RUNS) {
    insertTimes = new double[RUNS]
    upsertTimes = new double[RUNS]

    // clean the graph
    g.V().drop().iterate();

    // the first vertex takes longer due to initialization
    insertVertices(1)

    output = ""

    for (i = 0; i < RUNS; ++i) {
        insertTimes[i] = Double.POSITIVE_INFINITY;
        upsertTimes[i] = Double.POSITIVE_INFINITY;
        try {
            insertTimes[i] = insertVertices(9 * (int) Math.pow(10, i))
            upsertTimes[i] = upsertEdges(10)
        } catch (Exception e) {}
    }

    for (i = 0; i < RUNS; ++i) {
        output = output << String.format("RESULT v=%d action=insert entity=vertex amount=%d, time=%.3f\n",
            (int) Math.pow(10, i),
            9 * (int) Math.pow(10, i),
            insertTimes[i])
    }

    for (i = 0; i < RUNS; ++i) {
        output = output << String.format("RESULT v=%d action=upsert entity=edge amount=%d, time=%.3f\n",
            10 * (int) Math.pow(10, i),
            10,
            upsertTimes[i])
    }

    return output;
}; []

def supernodeBenchmark(RUNS) {
    insertTimes = new double[RUNS];
    upsertTimes = new double[RUNS];

    // clean the graph
    g.V().drop().iterate();

    // create supernode
    insertVertices(1)
    supernode = g.V().next()

    output = "\n"

    // the first vertex takes longer due to initialization
    insertVertices(1)
    upsertSupernodeEdges(supernode, 1)

    for (i = 0; i < RUNS; ++i) {
        insertTimes[i] = Double.POSITIVE_INFINITY;
        upsertTimes[i] = Double.POSITIVE_INFINITY;
        try {
            insertTimes[i] = insertVertices(9 * (int) Math.pow(10, i))
            upsertTimes[i] = upsertSupernodeEdges(supernode, 9 * (int) Math.pow(10, i))
        } catch (Exception e) {}
    }

    for (i = 0; i < RUNS; ++i) {
        output = output << String.format("RESULT v=%d action=insert entity=vertex amount=%d, time=%.3f\n",
            (int) Math.pow(10, i),
            9 * (int) Math.pow(10, i),
            insertTimes[i])
    }

    for (i = 0; i < RUNS; ++i) {
        output = output << String.format("RESULT v=%d action=upsertSupernode entity=edge amount=%d, time=%.3f\n",
            10 * (int) Math.pow(10, i),
            9 * (int) Math.pow(10, i),
            upsertTimes[i])
    }

    return output;
}; []
