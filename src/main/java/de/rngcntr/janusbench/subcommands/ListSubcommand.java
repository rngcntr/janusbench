package de.rngcntr.janusbench.subcommands;

import de.rngcntr.janusbench.backend.Index;
import de.rngcntr.janusbench.backend.Storage;
import de.rngcntr.janusbench.subcommands.ListSubcommand;
import de.rngcntr.janusbench.util.BenchmarkFactory;
import de.rngcntr.janusbench.util.ExitCode;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

@Command(name = "list", description = "Show all available instances of a given feature",
         exitCodeOnInvalidInput = ExitCode.INVALID_INPUT, sortOptions = false,
         usageHelpAutoWidth = true)
public class ListSubcommand implements Callable<Integer> {
    @Spec CommandSpec spec;

    public Integer call() throws Exception {
        spec.commandLine().usage(System.out);

        return ExitCode.INVALID_INPUT;
    }

    @Command(name = "storage",
             description = "Prints a list of supported storage backends to STDOUT.",
             sortOptions = false, usageHelpAutoWidth = true)
    public Integer listStorage() {
        for (Storage backend : Storage.values()) {
            System.out.println(backend.toString());
        }
        return 0;
    }

    @Command(name = "index", description = "Prints a list of supported index backends to STDOUT.",
             sortOptions = false, usageHelpAutoWidth = true)
    public Integer listIndex() {
        for (Index backend : Index.values()) {
            System.out.println(backend.toString());
        }
        return 0;
    }

    @Command(name = "benchmark", description = "Prints a list of implemented benchmarks to STDOUT.",
            sortOptions = false, usageHelpAutoWidth = true)
    public Integer listBenchmark() {
        for (String className : BenchmarkFactory.getRegisteredSuppliers()) {
            System.out.println(className);
        }
        return 0;
    }
}
