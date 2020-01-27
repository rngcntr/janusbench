public class UpsertRandomEdgeBenchmark extends AbstractBenchmark {
    private Vertex[] a;
    private Vertex[] b;

    private Random rand;

    public UpsertRandomEdgeBenchmark(GraphTraversalSource g) {
        super(g);
    }

    public UpsertRandomEdgeBenchmark(GraphTraversalSource g, int stepSize) {
        super(g, stepSize);
    }

    public void buildUp() {
        // prepare edges to insert
        a = new Vertex[stepSize];
        b = new Vertex[stepSize];
        
        // get a list of all vertices to select from
        ArrayList<Vertex> allVertices = g.V().toList()
        rand = new Random(System.nanoTime());

        for (int i = 0; i < stepSize; ++i) {
            // randomly choose an incoming vertex
            int selectedIndexA = rand.nextInt(allVertices.size())
            a[i] = allVertices[selectedIndexA]

            // one vertex less to sample from
            int selectedIndexB = rand.nextInt(allVertices.size() - 1)
            if (selectedIndexA == selectedIndexB) {
                // if the same index is selected, use another vertex instead
                selectedIndexB = allVertices.size() - 1;
            }
            b[i] = allVertices[selectedIndexB]
        }
    }

    public void performAction(AbstractBenchmark.BenchmarkResult result) {
        for (int index = 0; index < stepSize; ++index) {
            if (g.V(a[index]).in('knows').where(is(b[index])).hasNext()) {
                // edge already exists -> update
                Edge e = g.V(a[index]).inE('knows').as('e').outV().where(is(b[index])).select('e').next()
                e.property('lastSeen', new Date())
                e.property('timesSeen', rand.nextInt())
            } else {
                // edge does not exist -> insert
                g.addE('knows').from(a[index]).to(b[index]).
                    property('lastSeen', new Date()).
                    property('timesSeen', rand.nextInt()).
                    next()
            }
        }
    }

    public void tearDown() {
    }
}

