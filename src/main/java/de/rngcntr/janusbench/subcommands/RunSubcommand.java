package de.rngcntr.janusbench.subcommands;

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
import de.rngcntr.janusbench.util.ResultLogger;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.concurrent.Callable;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "run", description = "Runs a specified benchmark",
         exitCodeOnInvalidInput = ExitCode.INVALID_INPUT, sortOptions = false)
public class RunSubcommand implements Callable<Integer> {

    @Option(names = {"--remote-properties"}, paramLabel = "FILE",
            defaultValue = "conf/remote-graph.properties",
            description = "The remote graph properties file"
                          + "\ndefault: ${DEFAULT-VALUE}")
    private static File REMOTE_PROPERTIES;

    @Option(names = {"--native"},
            description = "This option instructs janusbench to use an existing environment and"
                          + " skip creating the services defined in local docker compose files.")
    private static boolean NATIVE = false;

    @Option(names = {"--schema-script"}, paramLabel = "FILE",
            defaultValue = "conf/initialize-graph.groovy",
            description = "The groovy script used for initialization of the graph schema"
                          + "\ndefault: ${DEFAULT-VALUE}")
    private static File INIT_SCRIPT;

    @Option(names = {"-o", "--output"}, paramLabel = "OUTPUT FILE",
            description = "The desired location to write the collected results."
                          + " If unassigned, results/results.txt will be used.")
    private static File OUTPUT_FILE;

    @Option(names = {"-s", "--storage"}, split = ",\\s*", paramLabel = "STORAGE BACKEND",
            required = true,
            description = "One of the supported storage backends."
                          + " For a list of supported storage backends use"
                          + " janusbench list storage"
                          + "\nAvailable: ${COMPLETION-CANDIDATES}")
    private static Storage[] STORAGE_BACKENDS;

    @Option(names = {"-i", "--index"}, split = ",\\s*", paramLabel = "INDEX BACKEND",
            defaultValue = "none",
            description = "One of the supported storage index."
                          + " For a list of supported index backends use"
                          + " janusbench list index"
                          + "\nAvailable: ${COMPLETION-CANDIDATES}")
    private static Index[] INDEX_BACKENDS;

    @Parameters(index = "0", paramLabel = "BENCHMARK CLASS", converter = {BenchmarkFactory.class},
                description = "The benchmark to run"
                              + "\nAvailable: ${COMPLETION-CANDIDATES}")
    private static Class<? extends Benchmark> benchmarkClass;

    private static final Logger log = Logger.getLogger(RunSubcommand.class);

    private Configuration configuration;
    private Connection connection;

    public Integer call() throws Exception {
        log.info("Using " + benchmarkClass.getName());

        // create result file
        try {
            if (OUTPUT_FILE != null) {
                ResultLogger.getInstance().setOutputMethod(OUTPUT_FILE);
            } else {
                ResultLogger.getInstance().setOutputMethod("results.txt");
            }
        } catch (final IOException ioex) {
            log.error("Unable to create results file");
            return ExitCode.INACCESSIBLE_RESULT_FILE;
        }

        // iterate over backend combinations
        for (Storage storage : STORAGE_BACKENDS) {
            for (Index index : INDEX_BACKENDS) {
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

                Benchmark benchmark =
                    BenchmarkFactory.getDefaultBenchmark(benchmarkClass, connection);
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

    public void createSchema() throws NoSchemaFoundException {
        log.info("Creating schema");
        try {
            String initRequest = new String(Files.readAllBytes(INIT_SCRIPT.toPath()));
            connection.submit(initRequest);
            log.info("Done creating schema");
        } catch (IOException ioex) {
            throw new NoSchemaFoundException("Schema not found: " + INIT_SCRIPT.getAbsolutePath(),
                                             ioex);
        }
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