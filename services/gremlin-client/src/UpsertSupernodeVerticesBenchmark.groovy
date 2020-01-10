public class UpsertSupernodeVerticesBenchmark extends AbstractBenchmark {
    private Vertex supernode;

    private String[] names;
    private int[] ages;

    private char[] charPool;
    private int minAge;
    private int maxAge;

    private Random rand;

    public UpsertSupernodeVerticesBenchmark(GraphTraversalSource g, Vertex supernode) {
        super(g);
        this.supernode = supernode;
    }

    public void buildUp(int amount) {
        names = new String[amount];
        ages = new int[amount];
        charPool = ['a'..'z', 'A'..'Z', 0..9].flatten();
        minAge = 18;
        maxAge = 100;
        rand = new Random(System.currentTimeMillis());

        for (int i = 0; i < amount; ++i) {
            char[] nameChars = (0..7).collect { charPool[rand.nextInt(charPool.size())] };
            names[i] = nameChars.join();
            ages[i] = rand.nextInt(maxAge - minAge) + minAge;
        }
    }

    public void performAction(int amount) {
        for (int index = 0; index < amount; ++index) {
            if (g.V(supernode).out('knows').has('name', names[index]).hasNext()) {
                // vertex already exists -> update edge
                Edge e = g.V(supernode).outE('knows').as('e').inV().has('name', names[index]).select('e').next()
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

    public void tearDown(int amount) {}
}
