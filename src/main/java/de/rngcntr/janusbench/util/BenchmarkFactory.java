package de.rngcntr.janusbench.util;

import de.rngcntr.janusbench.backend.Connection;
import de.rngcntr.janusbench.benchmark.complex.IndexedEdgeExistenceOnSupernode;
import de.rngcntr.janusbench.benchmark.simple.EdgeExistenceBenchmark;
import de.rngcntr.janusbench.benchmark.simple.InsertSupernodeVerticesBenchmark;
import de.rngcntr.janusbench.benchmark.simple.InsertVerticesBenchmark;
import de.rngcntr.janusbench.benchmark.simple.UpsertRandomEdgeBenchmark;
import de.rngcntr.janusbench.benchmark.simple.UpsertSupernodeEdgeBenchmark;
import de.rngcntr.janusbench.benchmark.simple.UpsertSupernodeVerticesBenchmark;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import picocli.CommandLine.ITypeConverter;

public class BenchmarkFactory implements ITypeConverter<Class<? extends Benchmark>> {

    private static final Map<String, Class<? extends Benchmark>> registeredBenchmarks;
    private static final Map<Class<? extends Benchmark>, Supplier<? extends Benchmark>> registeredSuppliers;

    static {
        registeredBenchmarks = new HashMap<String, Class<? extends Benchmark>>();
        registeredSuppliers = new HashMap<Class<? extends Benchmark>, Supplier<? extends Benchmark>>();
    }

    static {
        registerSupplier(EdgeExistenceBenchmark.class, (conn) -> new EdgeExistenceBenchmark<String>(conn, null, null, null));
        registerSupplier(InsertSupernodeVerticesBenchmark.class, (conn) -> new InsertSupernodeVerticesBenchmark(conn, null));
        registerSupplier(InsertVerticesBenchmark.class, (conn) -> new InsertVerticesBenchmark(conn));
        registerSupplier(UpsertRandomEdgeBenchmark.class, (conn) -> new UpsertRandomEdgeBenchmark(conn));
        registerSupplier(UpsertSupernodeEdgeBenchmark.class, (conn) -> new UpsertSupernodeEdgeBenchmark(conn, null));
        registerSupplier(UpsertSupernodeVerticesBenchmark.class, (conn) -> new UpsertSupernodeVerticesBenchmark(conn, null));

        registerSupplier(IndexedEdgeExistenceOnSupernode.class, (conn) -> new IndexedEdgeExistenceOnSupernode(conn, 10, 10, 10));
    }

    public static Set<String> getRegisteredSuppliers() { return registeredBenchmarks.keySet(); }

    public static void registerSupplier(Class<? extends Benchmark> benchmarkClass, Supplier<? extends Benchmark> supplier) {
        registeredBenchmarks.put(benchmarkClass.getSimpleName(), benchmarkClass);
        registeredSuppliers.put(benchmarkClass, supplier);
    }

    public static Benchmark getDefaultBenchmark(Class<? extends Benchmark> benchmarkClass, Connection conn) {
        Supplier<? extends Benchmark> supplier = registeredSuppliers.get(benchmarkClass);
        return supplier != null ? supplier.get(conn) : null;
    }

    @Override
    public Class<? extends Benchmark> convert(String className) throws Exception {
        return registeredBenchmarks.get(className);
    }
}