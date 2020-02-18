package de.rngcntr.janusbench.subcommands;

import de.rngcntr.janusbench.backend.Configuration;
import de.rngcntr.janusbench.backend.Connection;
import de.rngcntr.janusbench.backend.Index;
import de.rngcntr.janusbench.backend.Storage;
import de.rngcntr.janusbench.util.Benchmark;
import de.rngcntr.janusbench.util.BenchmarkFactory;
import de.rngcntr.janusbench.util.BenchmarkResult;
import de.rngcntr.janusbench.util.ResultLogger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "run", description = "Runs a specified benchmark")
public class RunSubcommand implements Callable<Integer> {

    @Option(names = {"--remote-properties"}, paramLabel = "FILE",
            defaultValue = "conf/remote-graph.properties",
            description = "The remote graph properties file"
                          + "\ndefault: ${DEFAULT-VALUE}")
    private static String REMOTE_PROPERTIES;

    @Option(names = {"--schema-script"}, paramLabel = "FILE",
            defaultValue = "conf/initialize-graph.groovy",
            description = "The groovy script used for initialization of the graph schema"
                          + "\ndefault: ${DEFAULT-VALUE}")
    private static String INIT_SCRIPT;

    @Option(names = {"-s", "--storage"}, paramLabel = "STORAGE BACKEND", required = true,
            description = "One of the supported storage backends."
                          + " For a list of supported storage backends use"
                          + "\njanusbench list storage")
    private static Storage STORAGE_BACKEND;

    @Option(names = {"-i", "--index"}, paramLabel = "INDEX BACKEND",
            description = "One of the supported storage index."
                          + " For a list of supported index backends use"
                          + "\njanusbench list index")
    private static Index INDEX_BACKEND;

    @Parameters(index = "0", paramLabel = "BENCHMARK CLASS", converter = {BenchmarkFactory.class},
                description = "The benchmark to run")
    private static Class<? extends Benchmark> benchmarkClass;

    private static final Logger log = Logger.getLogger(RunSubcommand.class);

    private Configuration configuration;
    private Connection connection;

    public Integer call() throws Exception {
        log.info("Using " + benchmarkClass.getName());

        configuration = new Configuration(STORAGE_BACKEND, INDEX_BACKEND);
        this.connection = new Connection(REMOTE_PROPERTIES);

        final boolean started = configuration.start();
        final boolean open = openGraph();

        if (started && open) {
            try {
                createSchema();
            } catch (final IOException ioex) {
                log.error("Graph initialization script not found: " + INIT_SCRIPT);
            }

            try {
                ResultLogger.getInstance().setOutputMethod("results.txt");
            } catch (final IOException e) {
                log.error("Unable to create results file");
                e.printStackTrace();
            }

            runBenchmark(BenchmarkFactory.getDefaultBenchmark(benchmarkClass, connection));
        }

        closeGraph();
        configuration.stop();
        return 0;
    }

    public void runBenchmark(final Benchmark benchmark) {
        benchmark.run();
        for (final BenchmarkResult br : benchmark.getResults()) {
            System.out.println(br);
        }
    }

    public void createSchema() throws IOException {
        log.info("Creating schema");
        final String initRequest = new String(Files.readAllBytes(Paths.get(INIT_SCRIPT)));
        connection.submit(initRequest);
        log.info("Done creating schema");
    }

    public boolean openGraph() {
        log.info("Opening graph");

        try {
            connection.open();
        } catch (final ConfigurationException cex) {
            log.error("Unable to connect to graph");
            log.error(cex);
            return false;
        }

        log.info("Successfully opened graph");
        return true;
    }

    public boolean closeGraph() {
        log.info("Closing graph");

        try {
            connection.close();
        } catch (final Exception ex) {
            log.error("Unable to close graph");
            log.error(ex);
            return false;
        }

        log.info("Successfully closed graph");
        return true;
    }
}