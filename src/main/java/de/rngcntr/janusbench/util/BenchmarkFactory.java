package de.rngcntr.janusbench.util;

import de.rngcntr.janusbench.backend.Connection;
import de.rngcntr.janusbench.benchmark.MicroBenchmarkRunner;
import de.rngcntr.janusbench.exceptions.UnavailableBenchmarkException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.testcontainers.shaded.org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.Yaml;
import picocli.CommandLine.ITypeConverter;

public class BenchmarkFactory implements ITypeConverter<String> {

    private static final String configPath = "conf/defaults/";
    private static final String configExtension = ".yml";
    private static final String benchmarkPackage = "de.rngcntr.janusbench.benchmark.composed.";

    private static final Map<String, Supplier<? extends Benchmark>> registeredSuppliers;

    private static final Logger log = Logger.getLogger(BenchmarkFactory.class);

    private static Yaml yaml;

    static {
        registeredSuppliers =
            new HashMap<String, Supplier<? extends Benchmark>>();

        yaml = new Yaml();
    }

    static { registerAllSuppliers(); }

    private static void registerAllSuppliers() {
        try (Stream<Path> walk = Files.walk(Paths.get(configPath))) {

            List<String> result = walk.filter(f -> f.toString().endsWith(configExtension))
                                      .map(f -> f.getFileName().toString())
                                      .map(f -> FilenameUtils.getBaseName(f))
                                      .collect(Collectors.toList());

            result.forEach(BenchmarkFactory::registerSupplier);

        } catch (IOException e) {
            log.warn("Cannot find default benchmark config path " + configPath +
                     ". No benchmarks will be available.");
        }
    }

    private static void registerSupplier(String benchmarkName) {
        String fullyQualifiedClassName = benchmarkPackage + benchmarkName;
        Class<?> retreivedClass;

        try {
            retreivedClass = Class.forName(fullyQualifiedClassName);
        } catch (ClassNotFoundException cnfex) {
            // micro benchmark files are not necessarily called "MicroBenchmark.yml"
            // therefore, try to interpret the configuration as a micro benchmark if no other class
            // is found
            retreivedClass = MicroBenchmarkRunner.class;
        }

        if (!Benchmark.class.isAssignableFrom(retreivedClass)) {
            log.warn("Known class " + fullyQualifiedClassName + " is not a benchmark");
            return;
        }

        @SuppressWarnings("unchecked")
        Class<? extends Benchmark> benchmarkClass = (Class<? extends Benchmark>) retreivedClass;

        registerSupplier(benchmarkName, (conn) -> {
            try {
                InputStream in =
                    Files.newInputStream(Paths.get(configPath + benchmarkName + configExtension));
                Benchmark benchmark = (Benchmark) yaml.loadAs(in, benchmarkClass);
                benchmark.setDisplayName(benchmarkName);
                benchmark.setConnection(conn);
                return benchmark;
            } catch (YAMLException yamlex) {
                log.error("Invalid configuration for class " + benchmarkClass.getSimpleName(),
                          yamlex);
                return null;
            } catch (NullPointerException npex) {
                log.error("Unable to parse yaml " + configPath + benchmarkName + configExtension +
                              " for class " + benchmarkClass.getSimpleName(),
                          npex);
                return null;
            } catch (IOException ioex) {
                log.error("Unable to find default configuration for class " +
                              fullyQualifiedClassName,
                          ioex);
                return null;
            }
        });
    }

    private static void registerSupplier(String benchmarkName,
                                         Supplier<? extends Benchmark> supplier) {
        registeredSuppliers.put(benchmarkName, supplier);
    }

    public static Set<String> getRegisteredSuppliers() { return registeredSuppliers.keySet(); }

    public static Benchmark getDefaultBenchmark(String benchmarkName, Connection conn) {
        Supplier<? extends Benchmark> supplier = registeredSuppliers.get(benchmarkName);
        if (supplier != null) {
            return supplier.get(conn);
        } else {
            throw new UnavailableBenchmarkException("Benchmark " + benchmarkName +
                                                    " has no registered default");
        }
    }

    @Override
    public String convert(String benchmarkName) {
        if (registeredSuppliers.containsKey(benchmarkName)) {
            return benchmarkName;
        } else {
            throw new UnavailableBenchmarkException("Benchmark " + benchmarkName + " does not exist");
        }
    }
}