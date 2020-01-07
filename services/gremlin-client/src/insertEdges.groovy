clock(100) {
    g.addE("route").from(g.V().sample(1).next()).to(g.V().sample(1).next())
    graph.tx().commit()
}
