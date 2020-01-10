public class UpsertRandomEdgeBenchmark extends AbstractBenchmark {
    private Vertex[] a;
    private Vertex[] b;

    private Random rand;

    public UpsertRandomEdgeBenchmark(GraphTraversalSource g) {
        super(g);
    }

    public void buildUp(int amount) {
        // prepare edges to insert
        a = new Vertex[amount];
        b = new Vertex[amount];
        
        // get a list of all vertices to select from
        ArrayList<Vertex> allVertices = g.V().toList()
        rand = new Random(System.currentTimeMillis());

        for (int i = 0; i < amount; ++i) {
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

    public void performAction(int amount) {
        for (int index = 0; index < amount; ++index) {
            if (g.V(a[index]).out('knows').where(is(b[index])).hasNext()) {
                // edge already exists -> update
                Edge e = g.V(a[index]).outE('knows').as('e').inV().where(is(b[index])).select('e').next()
                e.property('lastSeen', new Date())
            } else {
                // edge does not exist -> insert
                g.addE('knows').from(a[index]).to(b[index]).property('lastSeen', new Date()).next()
            }
        }
    }

    public void tearDown(int amount) {
    }
}

