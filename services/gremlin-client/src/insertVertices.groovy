def insertVertices (num) {
    println ">> Preparing Vertices"
    // prepare vertices to insert
    names = new String[num]; []
    ages  = new int[num]; []

    charPool = ['a'..'z', 'A'..'Z', 0..9].flatten(); []
    maxAge = 100; []
    minAge = 18; []
    rand = new Random(System.currentTimeMillis()); []

    for (i = 0; i < num; ++i) {
        nameChars = (0..7).collect { charPool[rand.nextInt(charPool.size())] }; []
        names[i] = nameChars.join(); []
        ages[i] = rand.nextInt(maxAge - minAge) + minAge; []
    }

    index = 0;

    println ">> Inserting Vertices"
    // statement within clock(N) is run N+1 times
    time = clock(num - 1) {
        g.addV('person').
            property('name', names[index]).
            property('age', ages[index++]).
            next()
    }

    println ">> Done"
    return time
}