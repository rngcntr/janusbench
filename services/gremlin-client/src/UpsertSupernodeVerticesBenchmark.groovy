public class UpsertSupernodeVerticesBenchmark extends AbstractBenchmark {
    private Vertex supernode;

    private String[] names;
    private int[] ages;

    private char[] charPool;
    private int minAge;
    private int maxAge;

    private Random rand;

    public UpsertSupernodeVerticesBenchmark(GraphTraversalSource g, int stepSize, Vertex supernode) {
        super(g, stepSize);
        this.supernode = supernode;
    }

    public UpsertSupernodeVerticesBenchmark(GraphTraversalSource g, Vertex supernode) {
        super(g);
        this.supernode = supernode;
    }

    public void buildUp() {
        names = new String[stepSize];
        ages = new int[stepSize];
        charPool = ['a'..'z', 'A'..'Z', 0..9].flatten();
        minAge = 18;
        maxAge = 100;
        rand = new Random(System.nanoTime());

        for (int i = 0; i < stepSize; ++i) {
            char[] nameChars = (0..7).collect { charPool[rand.nextInt(charPool.size())] };
            names[i] = nameChars.join();
            ages[i] = rand.nextInt(maxAge - minAge) + minAge;
        }
    }

    public void performAction(AbstractBenchmark.BenchmarkResult result) {
        for (int index = 0; index < stepSize; ++index) {
            if (g.V().has('name', names[index]).in('knows').where(is(supernode)).hasNext()) {
                // vertex already exists -> update edge
                Edge e = g.V().has('name', names[index]).inE('knows').as('e').outV().where(is(supernode)).select('e').next()
                e.property('lastSeen', new Date())
            } else {
                // vertex does not exist -> insert
                Vertex insertedVertex = g.addV('person').
                    property('name', names[index]).
                    property('age', ages[index]).
                    next()
                g.addE('knows').from(supernode).to(insertedVertex).property('lastSeen', new Date()).next()
            }
        }
    }

    public void tearDown() {}
}
