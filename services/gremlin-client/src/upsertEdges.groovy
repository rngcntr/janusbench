def upsertEdges (num) {
    println ">> Preparing Edges"
    // prepare edges to insert
    a = new org.apache.tinkerpop.gremlin.structure.Vertex[num]; []
    b = new org.apache.tinkerpop.gremlin.structure.Vertex[num]; []

    for (i = 0; i < num; ++i) {
        twoVertices = g.V().sample(2).toList()
        a[i] = twoVertices[0]
        b[i] = twoVertices[1]
    }

    index = 0;

    println ">> Inserting Edges"
    // statement within clock(N) is run N+1 times
    time = clock(num - 1) {
        if (g.V(a[index]).out('knows').where(is(b[index])).hasNext()) {
            // edge already exists -> update
            e = g.V(a[index]).outE('knows').as('e').inV().where(is(b[index])).select('e').next()
            e.property('lastSeen', new Date())
        } else {
            // edge does not exist -> insert
            g.addE('knows').from(a[index]).to(b[index++]).property('lastSeen', new Date()).next()
        }
    }

    println ">> Done"
    return time
}
