package de.rngcntr.janusbench.subcommands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.rngcntr.janusbench.backend.Index;
import de.rngcntr.janusbench.backend.Storage;
import de.rngcntr.janusbench.util.ExitCode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testcontainers.junit.jupiter.Testcontainers;
import picocli.CommandLine;

@Testcontainers
public class RunSubcommandBigTests {

    static Stream<Arguments> generateConfigurations() {
        List<Storage> storages = new ArrayList<Storage>();
        storages.addAll(Arrays.asList(Storage.values()));

        List<Index> indexes = new ArrayList<Index>();
        indexes.addAll(Arrays.asList(Index.values()));

        List<Arguments> configurations = new ArrayList<Arguments>();

        // special treatment for inmemory backend: don't combine with any index backend
        storages.remove(Storage.INMEMORY);
        configurations.add(Arguments.of(Storage.INMEMORY, null));

        storages.forEach(s -> indexes.forEach(i -> configurations.add(Arguments.of(s, i))));
        return configurations.stream();
    }

    @ParameterizedTest(name = "Run configuration {1}")
    @MethodSource("generateConfigurations")
    public void testContainerStartupWithJanusBench(Storage storage, Index index)
        throws InterruptedException {
        String[] args =
            index == null
                ? new String[] {"-s", storage.toString(), "InsertVertex"}
                : new String[] {"-s", storage.toString(), "-i", index.toString(),
                                "InsertVertex"};

        final RunSubcommand runner = new RunSubcommand();
        CommandLine cli = new CommandLine(runner);
        int returnCode = cli.execute(args);
        assertEquals(0, returnCode);

        Thread.sleep(15000);
    }

    @Test
    public void testValidStorageNoIndex() {
        String[] args = {"-s", "inmemory", "InsertVertex"};

        final RunSubcommand runner = new RunSubcommand();
        CommandLine cli = new CommandLine(runner);
        int returnCode = cli.execute(args);
        assertEquals(ExitCode.OK, returnCode);
    }

    @Test
    public void testMultipleValidStoragesNoIndex() {
        String[] args = {"-s", "inmemory, berkeleyje", "InsertVertex"};

        final RunSubcommand runner = new RunSubcommand();
        CommandLine cli = new CommandLine(runner);
        int returnCode = cli.execute(args);
        assertEquals(ExitCode.OK, returnCode);
    }

    @Test
    public void testValidStorageValidIndex() {
        String[] args = {"-s", "berkeleyje", "-i", "elasticsearch", "InsertVertex"};

        final RunSubcommand runner = new RunSubcommand();
        CommandLine cli = new CommandLine(runner);
        int returnCode = cli.execute(args);
        assertEquals(ExitCode.OK, returnCode);
    }

    @Test
    public void testValidStorageMultipleValidIndexes() {
        String[] args = {"-s", "berkeleyje", "-i", "elasticsearch, lucene", "InsertVertex"};

        final RunSubcommand runner = new RunSubcommand();
        CommandLine cli = new CommandLine(runner);
        int returnCode = cli.execute(args);
        assertEquals(ExitCode.OK, returnCode);
    }

    @Test
    public void testMultipleValidStoragesMultipleValidIndexes() {
        String[] args = {"-s", "berkeleyje, scylla", "-i", "elasticsearch, lucene", "InsertVertex"};

        final RunSubcommand runner = new RunSubcommand();
        CommandLine cli = new CommandLine(runner);
        int returnCode = cli.execute(args);
        assertEquals(ExitCode.OK, returnCode);
    }
}
