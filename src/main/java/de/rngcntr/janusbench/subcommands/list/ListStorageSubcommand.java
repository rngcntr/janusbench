package de.rngcntr.janusbench.subcommands.list;

import java.util.concurrent.Callable;
import picocli.CommandLine.Command;

@Command(name = "storage", description = "Prints a list of supported storage backends to STDOUT.")
public class ListStorageSubcommand implements Callable<Integer> {

    public Integer call() throws Exception {
        System.out.println("cassandra");
        System.out.println("scylla");
        System.out.println("berkeleyje");
        System.out.println("hbase");
        System.out.println("yugabyte");
        System.out.println("foundationdb");
        System.out.println("inmemory");

        return 0;
    }
}