package de.rngcntr.janusbench.subcommands.list;

import de.rngcntr.janusbench.util.BenchmarkFactory;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;

@Command(name = "benchmark", description = "Prints a list of implemented benchmarks to STDOUT.")
public class ListBenchmarkSubcommand implements Callable<Integer> {

    public Integer call() throws Exception {
        for (String className : BenchmarkFactory.getRegisteredSuppliers()) {
            System.out.println(className);
        }

        return 0;
    }
}