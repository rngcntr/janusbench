public class UpsertSupernodeEdgeBenchmark extends AbstractBenchmark {
    private Vertex supernode;
    private Vertex[] neighbours;

    private Random rand;

    public UpsertSupernodeEdgeBenchmark(GraphTraversalSource g, int stepSize, Vertex supernode) {
        super(g, stepSize);
        this.supernode = supernode;
    }

    public UpsertSupernodeEdgeBenchmark(GraphTraversalSource g, Vertex supernode) {
        super(g);
        this.supernode = supernode;
    }

    public void buildUp() {
        neighbours = new Vertex[stepSize];
        
        // get a list of all vertices to select from
        ArrayList<Vertex> allVertices = g.V().not(is(supernode)).toList()
        rand = new Random(System.nanoTime());

        for (int i = 0; i < stepSize; ++i) {
            // randomly choose an incoming vertex
            int selectedIndex = rand.nextInt(allVertices.size())
            neighbours[i] = allVertices[selectedIndex]
        }
    }

    public void performAction(AbstractBenchmark.BenchmarkResult result) {
        for (int index = 0; index < stepSize; ++index) {
            if (g.V(neighbours[index]).in('knows').where(is(supernode)).hasNext()) {
                // edge already exists -> update
                Edge e = g.V(neighbours[index]).inE('knows').as('e').outV().where(is(supernode)).select('e').next()
                e.property('lastSeen', new Date())
                e.property('timesSeen', rand.nextInt())
            } else {
                // edge does not exist -> insert
                g.addE('knows').from(supernode).to(neighbours[index]).
                    property('lastSeen', new Date()).
                    property('timesSeen', rand.nextInt()).
                    next()
            }
        }
    }

    public void tearDown() {
    }
}
