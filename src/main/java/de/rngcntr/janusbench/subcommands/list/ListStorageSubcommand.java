package de.rngcntr.janusbench.subcommands.list;

import de.rngcntr.janusbench.backend.Storage;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;

@Command(name = "storage", description = "Prints a list of supported storage backends to STDOUT.")
public class ListStorageSubcommand implements Callable<Integer> {

    public Integer call() throws Exception {
        for (Storage backend : Storage.values()) {
            System.out.println(backend.toString());
        }

        return 0;
    }
}