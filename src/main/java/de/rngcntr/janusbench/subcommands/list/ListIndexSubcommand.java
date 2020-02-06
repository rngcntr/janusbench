package de.rngcntr.janusbench.subcommands.list;

import de.rngcntr.janusbench.backend.Index;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;

@Command(name = "index", description = "Prints a list of supported index backends to STDOUT.")
public class ListIndexSubcommand implements Callable<Integer> {

    public Integer call() throws Exception {
        for (Index backend : Index.values()) {
            System.out.println(backend.toString());
        }

        return 0;
    }
}