clock(1) {
    g.V().has('airport', 'code', 'AUS').
        repeat(out()).until(has('code', 'AGR')).
        limit(10).path().by('code').toList()
}
