public class InsertVerticesBenchmark extends AbstractBenchmark {
    private String[] names;
    private int[] ages;

    private char[] charPool;
    private int minAge;
    private int maxAge;

    private Random rand;

    public InsertVerticesBenchmark(GraphTraversalSource g) {
        super(g);
    }

    public InsertVerticesBenchmark(GraphTraversalSource g, int stepSize) {
        super(g, stepSize);
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

    public void performAction(BenchmarkResult result) {
        for (int index = 0; index < stepSize; ++index) {
            g.addV('person').
                property('name', names[index]).
                property('age', ages[index]).
                next()
        }
    }

    public void tearDown() {}
}
