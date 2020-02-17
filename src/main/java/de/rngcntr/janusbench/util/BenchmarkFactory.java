package de.rngcntr.janusbench.util;

import de.rngcntr.janusbench.backend.Connection;
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

public class BenchmarkFactory implements ITypeConverter<Class<? extends Benchmark>> {

    private static final String configPath = "conf/defaults/";
    private static final String configExtension = ".yml";
    private static final String benchmarkPackage = "de.rngcntr.janusbench.benchmark.composed.";

    private static final Map<String, Class<? extends Benchmark>> registeredBenchmarks;
    private static final Map<Class<? extends Benchmark>, Supplier<? extends Benchmark>>
        registeredSuppliers;

    private static final Logger log = Logger.getLogger(BenchmarkFactory.class);

    private static Yaml yaml;

    static {
        registeredBenchmarks = new HashMap<String, Class<? extends Benchmark>>();
        registeredSuppliers =
            new HashMap<Class<? extends Benchmark>, Supplier<? extends Benchmark>>();

        yaml = new Yaml();
    }

    static {
        registerAllSuppliers();
    }

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

    private static void registerSupplier(String className) {
        String fullyQualifiedClassName = benchmarkPackage + className;
        Class<?> retreivedClass;
        
        try {
            retreivedClass = Class.forName(fullyQualifiedClassName);
        } catch (ClassNotFoundException cnfex) {
            log.warn("Unknown class " + fullyQualifiedClassName, cnfex);
            return;
        }

        if (!Benchmark.class.isAssignableFrom(retreivedClass)) {
            log.warn("Known class " + fullyQualifiedClassName + " is not a benchmark");
            return;
        }

        @SuppressWarnings("unchecked")
        Class<? extends Benchmark> benchmarkClass = (Class<? extends Benchmark>) retreivedClass;

        try {
            InputStream in = Files.newInputStream(Paths.get(configPath + className + configExtension));
            registerSupplier(benchmarkClass, (conn) -> {
                try{
                    Benchmark benchmark = (Benchmark) yaml.loadAs(in, benchmarkClass);
                    benchmark.setConnection(conn);
                    return benchmark;
                } catch (YAMLException yamlex) {
                    log.error("Invalid configuration for class " + benchmarkClass.getSimpleName(), yamlex); 
                    return null;
                }
            });
        } catch (IOException ioex) {
            log.warn("Unable to find default configuration for class " + fullyQualifiedClassName,
                     ioex);
        }
    }

    private static void registerSupplier(Class<? extends Benchmark> benchmarkClass, Supplier<? extends Benchmark> supplier) {
        registeredBenchmarks.put(benchmarkClass.getSimpleName(), benchmarkClass);
        registeredSuppliers.put(benchmarkClass, supplier);
    }

    public static Set<String> getRegisteredSuppliers() { return registeredBenchmarks.keySet(); }

    public static Benchmark getDefaultBenchmark(Class<? extends Benchmark> benchmarkClass, Connection conn) {
        Supplier<? extends Benchmark> supplier = registeredSuppliers.get(benchmarkClass);
        return supplier != null ? supplier.get(conn) : null;
    }

    @Override
    public Class<? extends Benchmark> convert(String className) throws Exception {
        return registeredBenchmarks.get(className);
    }
}