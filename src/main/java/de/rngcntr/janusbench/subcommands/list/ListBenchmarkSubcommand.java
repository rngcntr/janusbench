package de.rngcntr.janusbench.subcommands.list;

import de.rngcntr.janusbench.util.Benchmark;
import de.rngcntr.janusbench.util.ComposedBenchmark;
import java.util.concurrent.Callable;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "benchmark", description = "Prints a list of implemented benchmarks to STDOUT.")
public class ListBenchmarkSubcommand implements Callable<Integer> {

    @Option(names = {"-s", "--simple-only"}, description = "show simple benchmarks only")
    boolean simpleOnly = false;

    @Option(names = {"-c", "--complex-only"}, description = "show complex benchmarks only")
    boolean complexOnly = false;

    public Integer call() throws Exception {
        if (!complexOnly) {
            Reflections reflections =
                new Reflections("de.rngcntr.janusbench.benchmark.simple",
                                this.getClass().getClassLoader(), new SubTypesScanner());
            for (Class<? extends Benchmark> bClass : reflections.getSubTypesOf(Benchmark.class)) {
                System.out.println(bClass.getSimpleName());
            }
        }

        if (!simpleOnly) {
            Reflections reflections =
                new Reflections("de.rngcntr.janusbench.benchmark.complex",
                                this.getClass().getClassLoader(), new SubTypesScanner());
            for (Class<? extends Benchmark> bClass :
                 reflections.getSubTypesOf(ComposedBenchmark.class)) {
                System.out.println(bClass.getSimpleName());
            }
        }

        return 0;
    }
}