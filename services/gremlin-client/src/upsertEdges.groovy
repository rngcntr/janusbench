def upsertEdges (num) {
    // statement within clock(N) is run N+1 times
    clock(num - 1) {
        a = g.V().sample(1).next()
        b = g.V().not(is(a)).sample(1).next()

        if (g.V(a).out('knows').where(is(b)).hasNext()) {
            // edge already exists -> update
            e = g.V(a).outE('knows').as('e').inV().where(is(b)).select('e').next()
            e.property('lastSeen', new Date())
        } else {
            // edge does not exist -> insert
            g.addE('knows').from(a).to(b).property('lastSeen', new Date()).next()
        }
    }
}
