package de.rngcntr.janusbench.subcommands;

import de.rngcntr.janusbench.subcommands.ListSubcommand;
import de.rngcntr.janusbench.subcommands.list.ListBenchmarkSubcommand;
import de.rngcntr.janusbench.subcommands.list.ListIndexSubcommand;
import de.rngcntr.janusbench.subcommands.list.ListStorageSubcommand;
import de.rngcntr.janusbench.util.ExitCode;

import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

@Command(name = "list", description = "Show all available instances of a given feature",
         subcommands = {ListBenchmarkSubcommand.class, ListStorageSubcommand.class,
                        ListIndexSubcommand.class},
         exitCodeOnInvalidInput = ExitCode.INVALID_INPUT)
public class ListSubcommand implements Callable<Integer> {
    @Spec CommandSpec spec;

    public Integer call() throws Exception {
        spec.commandLine().usage(System.out);

        return ExitCode.INVALID_INPUT;
    }
}
