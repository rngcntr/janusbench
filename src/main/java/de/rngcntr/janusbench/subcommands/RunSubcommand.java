package de.rngcntr.janusbench.subcommands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.concurrent.Callable;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import de.rngcntr.janusbench.backend.Connection;
import de.rngcntr.janusbench.backend.Index;
import de.rngcntr.janusbench.backend.Storage;
import de.rngcntr.janusbench.backend.configuration.ComposeConfiguration;
import de.rngcntr.janusbench.backend.configuration.Configuration;
import de.rngcntr.janusbench.backend.configuration.NativeConfiguration;
import de.rngcntr.janusbench.exceptions.InvalidConfigurationException;
import de.rngcntr.janusbench.exceptions.NoSchemaFoundException;
import de.rngcntr.janusbench.util.Benchmark;
import de.rngcntr.janusbench.util.BenchmarkFactory;
import de.rngcntr.janusbench.util.ExitCode;
import de.rngcntr.janusbench.util.GraphLoader;
import de.rngcntr.janusbench.util.ResultLogger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "run", description = "Runs a specified benchmark",
         exitCodeOnInvalidInput = ExitCode.INVALID_INPUT, sortOptions = false,
         usageHelpAutoWidth = true)
public class RunSubcommand implements Callable<Integer> {

    @Option(names = {"--remote-properties"}, paramLabel = "FILE",
            defaultValue = "conf/remote-graph.properties",
            description = "The remote graph properties file"
                          + "\ndefault: ${DEFAULT-VALUE}")
    private File REMOTE_PROPERTIES;

    @Option(names = {"--native"},
            description = "This option instructs janusbench to use an existing environment and"
                          + " skip creating the services defined in local docker compose files.")
    private boolean NATIVE = false;

    @Option(names = {"--schema-script"}, paramLabel = "FILE",
            description = "The groovy script used for initialization of the graph schema"
                          + "\ndefault: ${DEFAULT-VALUE}")
    private File INIT_SCRIPT;

    @Option(names = {"-o", "--output"}, paramLabel = "OUTPUT FILE",
            description = "The desired location to write the collected results."
                          + " If unassigned, results/results.txt will be used.")
    private File OUTPUT_FILE;

    @Option(names = {"-s", "--storage"}, split = ",", paramLabel = "STORAGE BACKEND",
            required = true,
            description = "One of the supported storage backends."
                          + " For a list of supported storage backends use"
                          + " janusbench list storage"
                          + "\nAvailable: ${COMPLETION-CANDIDATES}")
    private Storage[] STORAGE_BACKENDS;

    @Option(names = {"-i", "--index"}, split = ",", paramLabel = "INDEX BACKEND",
            defaultValue = "none",
            description = "One of the supported storage index."
                          + " For a list of supported index backends use"
                          + " janusbench list index"
                          + "\nAvailable: ${COMPLETION-CANDIDATES}")
    private Index[] INDEX_BACKENDS;

    @Option(names = "-v", description = {"Specify multiple -v options to increase verbosity.",
                                         "Accepts -v up to -vvvv"})
    private boolean[] VERBOSITY = new boolean[]{};

    @Option(names = {"-g", "--initial-graph"}, paramLabel = "INITIAL GRAPH",
            converter = {GraphLoader.GraphLoaderConverter.class},
            description = "Initial state of the graph before running the benchmark."
                          // TODO: use picocli completion candidates for allowed types
                          + "\nAllowed Formats: GraphSON, GraphML, KRYO"
                          + "\nThis option is not available in combination with the --native flag")
    private GraphLoader INITIAL_GRAPH_LOADER;

    @Parameters(index = "0", paramLabel = "BENCHMARK CLASS", converter = {BenchmarkFactory.class},
                description = "The benchmark to run. See `janusbench list benchmark` for a list of valid benchmarks")
    private String benchmarkName;

    private final Logger log = Logger.getLogger(RunSubcommand.class);

    private Configuration configuration;
    private Connection connection;

    public Integer call() throws Exception {
        initializeLogger();

        try {
            initializeResultOutput();
        } catch (final IOException ioex) {
            log.error("Unable to create results file");
            return ExitCode.INACCESSIBLE_RESULT_FILE;
        }

        // create the directory before the docker environment start because it will be owned by root
        // otherwise
        GraphLoader.makeExchangeDirectory();

        log.info("Using " + benchmarkName);

        // iterate over backend combinations
        for (Storage storage : STORAGE_BACKENDS) {
            for (Index index : INDEX_BACKENDS) {
                log.info(String.format("Running on storage=%s and index=%s", storage, index));
                try {
                    initializeConfiguration(storage, index);
                } catch (final InvalidConfigurationException icex) {
                    log.error("Invalid configuration: " + storage.toString() + " and " +
                              index.toString() + " are incompatible.");
                    return ExitCode.INCOMPATIBLE_BACKENDS;
                }

                int exitCode = runConfiguration(storage, index);
                if (exitCode != ExitCode.OK) {
                    return exitCode;
                }

                configuration.stop();
            }
        }

        ResultLogger.getInstance().close();
        return ExitCode.OK;
    }

    private void initializeLogger() {
        Logger root = Logger.getRootLogger();
        root.removeAllAppenders();
        PatternLayout layout = new PatternLayout("%d [%t] %-5p %c - %m%n");
        ConsoleAppender stderr = new ConsoleAppender(layout, "System.err");
        root.addAppender(stderr);
        
        switch (VERBOSITY.length) {
        case 1:
            root.setLevel(Level.WARN);
            break;
        case 2:
            root.setLevel(Level.INFO);
            break;
        case 3:
            root.setLevel(Level.DEBUG);
            break;
        case 4:
            root.setLevel(Level.TRACE);
            break;
        default:
            root.setLevel(Level.ERROR);
        }
    }

    private void initializeResultOutput() throws IOException {
        if (OUTPUT_FILE != null) {
            ResultLogger.getInstance().setOutputMethod(OUTPUT_FILE);
        } else {
            ResultLogger.getInstance().setOutputMethod(System.out);
        }
    }

    private void initializeConfiguration(Storage storage, Index index)
        throws InvalidConfigurationException {
        if (NATIVE) {
            configuration = new NativeConfiguration(storage, index);
        } else {
            configuration = new ComposeConfiguration(storage, index);
        }
        configuration.setTimeout(Duration.ofMinutes(1));
    }

    private int runConfiguration(Storage storage, Index index) {

        this.connection = new Connection(REMOTE_PROPERTIES);

        final boolean started = configuration.start();
        final boolean open = openGraph();

        try {
            if (!started) {
                log.error("Unable to start configuration");
                return ExitCode.SERVICE_SETUP_ERROR;
            } else if (!open) {
                log.error("Unable to open graph");
                return ExitCode.SERVICE_SETUP_ERROR;
            } else {
                createSchema();
                loadInitialGraph();

                Benchmark benchmark =
                    BenchmarkFactory.getDefaultBenchmark(benchmarkName, connection);
                benchmark.setConfiguration(configuration);
                benchmark.run();

                return ExitCode.OK;
            }
        } catch (final NoSchemaFoundException nsfex) {
            log.error("Graph initialization script not found: " + INIT_SCRIPT.getAbsolutePath());
            return ExitCode.MISSING_SCHEMA_FILE;
        } finally {
            closeGraph();
        }
    }

    private void createSchema() throws NoSchemaFoundException {
        if (INIT_SCRIPT != null) {
            log.info("Creating schema");
            try {
                String initRequest = new String(Files.readAllBytes(INIT_SCRIPT.toPath()));
                connection.submit(initRequest);
                log.info("Done creating schema");
            } catch (IOException ioex) {
                throw new NoSchemaFoundException(
                    "Schema not found: " + INIT_SCRIPT.getAbsolutePath(), ioex);
            }
        }
    }

    private void loadInitialGraph() {
        log.info("Loading initial graph...");
        if (INITIAL_GRAPH_LOADER != null) {
            try {
                INITIAL_GRAPH_LOADER.loadGraph(connection);
                log.info("Done loading initial graph.");
            } catch (final Exception ex) {
                log.warn("Failed to load initial graph. Continuing with empty graph.");
            }
        }
    }

    private boolean openGraph() {
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

    private boolean closeGraph() {
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