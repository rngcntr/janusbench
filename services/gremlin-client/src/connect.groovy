:remote connect tinkerpop.server conf/remote.yaml session-managed
:remote console

while (true) {
    try {
        g;[] // wait for g to be available
        break;
    } catch (Exception e) {
        Thread.sleep(1000);[]
    }
}
