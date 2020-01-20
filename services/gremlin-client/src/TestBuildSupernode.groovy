ivb = new InsertVerticesBenchmark(g, 1)
ivb.run()

supernode = g.V().next()

threads = new Thread[5]

for (idx = 0; idx < 5; ++idx) {
    threads[idx] = Thread.start {
        iseb = new InsertSupernodeVerticesBenchmark(g, 10000, supernode);
        iseb.buildUp();
        iseb.performAction();
        g.tx().commit();
    }
}
for (idx = 0; idx < 5; ++idx) {
    threads[idx].join();
}

g.V(supernode).out().count().profile()

for (idx = 0; idx < 5; ++idx) {
    threads[idx] = Thread.start {
        iseb = new InsertSupernodeVerticesBenchmark(g, 10000, supernode);
        iseb.buildUp();
        iseb.performAction();
        g.tx().commit();
    }
}
for (idx = 0; idx < 5; ++idx) {
    threads[idx].join();
}

g.V(supernode).out().count().profile()

for (idx = 0; idx < 5; ++idx) {
    threads[idx] = Thread.start {
        iseb = new InsertSupernodeVerticesBenchmark(g, 10000, supernode);
        iseb.buildUp();
        iseb.performAction();
        g.tx().commit();
    }
}
for (idx = 0; idx < 5; ++idx) {
    threads[idx].join();
}

g.V(supernode).out().count().profile()

for (idx = 0; idx < 5; ++idx) {
    threads[idx] = Thread.start {
        iseb = new InsertSupernodeVerticesBenchmark(g, 10000, supernode);
        iseb.buildUp();
        iseb.performAction();
        g.tx().commit();
    }
}
for (idx = 0; idx < 5; ++idx) {
    threads[idx].join();
}

g.V(supernode).out().count().profile()

for (idx = 0; idx < 5; ++idx) {
    threads[idx] = Thread.start {
        iseb = new InsertSupernodeVerticesBenchmark(g, 10000, supernode);
        iseb.buildUp();
        iseb.performAction();
        g.tx().commit();
    }
}
for (idx = 0; idx < 5; ++idx) {
    threads[idx].join();
}

g.V(supernode).out().count().profile()

for (idx = 0; idx < 5; ++idx) {
    threads[idx] = Thread.start {
        iseb = new InsertSupernodeVerticesBenchmark(g, 10000, supernode);
        iseb.buildUp();
        iseb.performAction();
        g.tx().commit();
    }
}
for (idx = 0; idx < 5; ++idx) {
    threads[idx].join();
}

g.V(supernode).out().count().profile()

for (idx = 0; idx < 5; ++idx) {
    threads[idx] = Thread.start {
        iseb = new InsertSupernodeVerticesBenchmark(g, 10000, supernode);
        iseb.buildUp();
        iseb.performAction();
        g.tx().commit();
    }
}
for (idx = 0; idx < 5; ++idx) {
    threads[idx].join();
}

g.V(supernode).out().count().profile()

for (idx = 0; idx < 5; ++idx) {
    threads[idx] = Thread.start {
        iseb = new InsertSupernodeVerticesBenchmark(g, 10000, supernode);
        iseb.buildUp();
        iseb.performAction();
        g.tx().commit();
    }
}
for (idx = 0; idx < 5; ++idx) {
    threads[idx].join();
}

g.V(supernode).out().count().profile()

for (idx = 0; idx < 5; ++idx) {
    threads[idx] = Thread.start {
        iseb = new InsertSupernodeVerticesBenchmark(g, 10000, supernode);
        iseb.buildUp();
        iseb.performAction();
        g.tx().commit();
    }
}
for (idx = 0; idx < 5; ++idx) {
    threads[idx].join();
}

g.V(supernode).out().count().profile()

for (idx = 0; idx < 5; ++idx) {
    threads[idx] = Thread.start {
        iseb = new InsertSupernodeVerticesBenchmark(g, 10000, supernode);
        iseb.buildUp();
        iseb.performAction();
        g.tx().commit();
    }
}
for (idx = 0; idx < 5; ++idx) {
    threads[idx].join();
}

g.V(supernode).out().count().profile()

for (idx = 0; idx < 5; ++idx) {
    threads[idx] = Thread.start {
        iseb = new InsertSupernodeVerticesBenchmark(g, 10000, supernode);
        iseb.buildUp();
        iseb.performAction();
        g.tx().commit();
    }
}
for (idx = 0; idx < 5; ++idx) {
    threads[idx].join();
}

g.V(supernode).out().count().profile()

for (idx = 0; idx < 5; ++idx) {
    threads[idx] = Thread.start {
        iseb = new InsertSupernodeVerticesBenchmark(g, 10000, supernode);
        iseb.buildUp();
        iseb.performAction();
        g.tx().commit();
    }
}
for (idx = 0; idx < 5; ++idx) {
    threads[idx].join();
}

g.V(supernode).out().count().profile()

for (idx = 0; idx < 5; ++idx) {
    threads[idx] = Thread.start {
        iseb = new InsertSupernodeVerticesBenchmark(g, 10000, supernode);
        iseb.buildUp();
        iseb.performAction();
        g.tx().commit();
    }
}
for (idx = 0; idx < 5; ++idx) {
    threads[idx].join();
}

g.V(supernode).out().count().profile()

for (idx = 0; idx < 5; ++idx) {
    threads[idx] = Thread.start {
        iseb = new InsertSupernodeVerticesBenchmark(g, 10000, supernode);
        iseb.buildUp();
        iseb.performAction();
        g.tx().commit();
    }
}
for (idx = 0; idx < 5; ++idx) {
    threads[idx].join();
}

g.V(supernode).out().count().profile()

for (idx = 0; idx < 5; ++idx) {
    threads[idx] = Thread.start {
        iseb = new InsertSupernodeVerticesBenchmark(g, 10000, supernode);
        iseb.buildUp();
        iseb.performAction();
        g.tx().commit();
    }
}
for (idx = 0; idx < 5; ++idx) {
    threads[idx].join();
}

g.V(supernode).out().count().profile()

for (idx = 0; idx < 5; ++idx) {
    threads[idx] = Thread.start {
        iseb = new InsertSupernodeVerticesBenchmark(g, 10000, supernode);
        iseb.buildUp();
        iseb.performAction();
        g.tx().commit();
    }
}
for (idx = 0; idx < 5; ++idx) {
    threads[idx].join();
}

g.V(supernode).out().count().profile()

for (idx = 0; idx < 5; ++idx) {
    threads[idx] = Thread.start {
        iseb = new InsertSupernodeVerticesBenchmark(g, 10000, supernode);
        iseb.buildUp();
        iseb.performAction();
        g.tx().commit();
    }
}
for (idx = 0; idx < 5; ++idx) {
    threads[idx].join();
}

g.V(supernode).out().count().profile()

for (idx = 0; idx < 5; ++idx) {
    threads[idx] = Thread.start {
        iseb = new InsertSupernodeVerticesBenchmark(g, 10000, supernode);
        iseb.buildUp();
        iseb.performAction();
        g.tx().commit();
    }
}
for (idx = 0; idx < 5; ++idx) {
    threads[idx].join();
}

g.V(supernode).out().count().profile()

for (idx = 0; idx < 5; ++idx) {
    threads[idx] = Thread.start {
        iseb = new InsertSupernodeVerticesBenchmark(g, 10000, supernode);
        iseb.buildUp();
        iseb.performAction();
        g.tx().commit();
    }
}
for (idx = 0; idx < 5; ++idx) {
    threads[idx].join();
}

g.V(supernode).out().count().profile()

for (idx = 0; idx < 5; ++idx) {
    threads[idx] = Thread.start {
        iseb = new InsertSupernodeVerticesBenchmark(g, 10000, supernode);
        iseb.buildUp();
        iseb.performAction();
        g.tx().commit();
    }
}
for (idx = 0; idx < 5; ++idx) {
    threads[idx].join();
}

g.V(supernode).out().count().profile()

for (idx = 0; idx < 5; ++idx) {
    threads[idx] = Thread.start {
        iseb = new InsertSupernodeVerticesBenchmark(g, 10000, supernode);
        iseb.buildUp();
        iseb.performAction();
        g.tx().commit();
    }
}
for (idx = 0; idx < 5; ++idx) {
    threads[idx].join();
}

g.V(supernode).out().count().profile()

for (idx = 0; idx < 5; ++idx) {
    threads[idx] = Thread.start {
        iseb = new InsertSupernodeVerticesBenchmark(g, 10000, supernode);
        iseb.buildUp();
        iseb.performAction();
        g.tx().commit();
    }
}
for (idx = 0; idx < 5; ++idx) {
    threads[idx].join();
}

g.V(supernode).out().count().profile()
