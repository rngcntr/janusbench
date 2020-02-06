package de.rngcntr.janusbench.subcommands.list;

import java.util.concurrent.Callable;
import picocli.CommandLine.Command;

@Command(name = "index", description = "Prints a list of supported index backends to STDOUT.")
public class ListIndexSubcommand implements Callable<Integer> {

    public Integer call() throws Exception {
        System.out.println("elasticsearch");
        System.out.println("solr");
        System.out.println("lucene");

        return 0;
    }
}