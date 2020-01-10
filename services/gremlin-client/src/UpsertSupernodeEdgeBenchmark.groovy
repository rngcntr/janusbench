public class UpsertSupernodeEdgeBenchmark extends AbstractBenchmark {
    private Vertex supernode;
    private Vertex[] neighbours;

    private Random rand;

    public UpsertSupernodeEdgeBenchmark(GraphTraversalSource g, Vertex supernode) {
        super(g);
        this.supernode = supernode;
    }

    public void buildUp(int amount) {
        neighbours = new Vertex[amount];
        
        // get a list of all vertices to select from
        ArrayList<Vertex> allVertices = g.V().not(is(supernode)).toList()
        rand = new Random(System.currentTimeMillis());

        for (int i = 0; i < amount; ++i) {
            // randomly choose an incoming vertex
            int selectedIndex = rand.nextInt(allVertices.size())
            neighbours[i] = allVertices[selectedIndex]
        }
    }

    public void performAction(int amount) {
        for (int index = 0; index < amount; ++index) {
            if (g.V(supernode).out('knows').where(is(neighbours[index])).hasNext()) {
                // edge already exists -> update
                Edge e = g.V(supernode).outE('knows').as('e').inV().where(is(neighbours[index])).select('e').next()
                e.property('lastSeen', new Date())
            } else {
                // edge does not exist -> insert
                g.addE('knows').from(supernode).to(neighbours[index]).property('lastSeen', new Date()).next()
            }
        }
    }

    public void tearDown(int amount) {
    }
}
