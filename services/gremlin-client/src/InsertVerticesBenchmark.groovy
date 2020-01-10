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
            g.addV('person').
                property('name', names[index]).
                property('age', ages[index]).
                next()
        }
    }

    public void tearDown(int amount) {}
}
