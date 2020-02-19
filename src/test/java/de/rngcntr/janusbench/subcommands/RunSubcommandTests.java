package de.rngcntr.janusbench.subcommands;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
public class RunSubcommandTests {

    public Stream<Arguments> generateConfigurations() {
        List<Storage> storages = Arrays.asList(Storage.values());
        List<Index> indexes = Arrays.asList(Index.values());
        List<Arguments> configurations = new ArrayList<Arguments>();

        // special treatment for inmemory backend: don't combine with any index backend
        storages.remove(Storage.INMEMORY);
        configurations.add(Arguments.of(Storage.INMEMORY, null));

        storages.forEach(s -> indexes.forEach(i -> configurations.add(Arguments.of(s, i))));
        return configurations.stream();
    }

    @ParameterizedTest(name = "Run configuration {0}")
    @MethodSource("generateConfigurations")
    public void testContainerStartupWithJanusBench(Storage storage, Index index)  {
        String[] args =
            index == null
                ? new String[] {"-s", storage.toString(), "IndexedEdgeExistenceOnSupernode"}
                : new String[] {"-s", storage.toString(), "-i", index.toString(),
                                "IndexedEdgeExistenceOnSupernode"};

        final RunSubcommand runner = assertDoesNotThrow(() -> new RunSubcommand());
        CommandLine cli = new CommandLine(runner);
        int returnCode = cli.execute(args);
        assertEquals(0, returnCode);
    }

    @Test
    public void testNoStorageNoIndex()  {
        String[] args = {"IndexedEdgeExistenceOnSupernode"};

        final RunSubcommand runner = assertDoesNotThrow(() -> new RunSubcommand());
        CommandLine cli = new CommandLine(runner);
        int returnCode = cli.execute(args);
        assertEquals(ExitCode.INVALID_INPUT, returnCode);
    }

    @Test
    public void testInvalidStorageValidIndex()  {
        String[] args = {"-s", "nonExistentStorage", "-i", "elasticsearch",
                         "IndexedEdgeExistenceOnSupernode"};

        final RunSubcommand runner = assertDoesNotThrow(() -> new RunSubcommand());
        CommandLine cli = new CommandLine(runner);
        int returnCode = cli.execute(args);
        assertEquals(ExitCode.INVALID_INPUT, returnCode);
    }

    @Test
    public void testInvalidStorageNoIndex()  {
        String[] args = {"-s", "nonExistentStorage", "IndexedEdgeExistenceOnSupernode"};

        final RunSubcommand runner = assertDoesNotThrow(() -> new RunSubcommand());
        CommandLine cli = new CommandLine(runner);
        int returnCode = cli.execute(args);
        assertEquals(ExitCode.INVALID_INPUT, returnCode);
    }

    @Test
    public void testValidStorageInvalidIndex()  {
        String[] args = {"-s", "cassandra", "-i", "nonExistentIndex",
                         "IndexedEdgeExistenceOnSupernode"};

        final RunSubcommand runner = assertDoesNotThrow(() -> new RunSubcommand());
        CommandLine cli = new CommandLine(runner);
        int returnCode = cli.execute(args);
        assertEquals(ExitCode.INVALID_INPUT, returnCode);
    }

    @Test
    public void testValidStorageInvalidSchema()  {
        String[] args = {"-s", "inmemory", "--schema-script", "nonExistentSchemaScript",
                         "IndexedEdgeExistenceOnSupernode"};

        final RunSubcommand runner = assertDoesNotThrow(() -> new RunSubcommand());
        CommandLine cli = new CommandLine(runner);
        int returnCode = cli.execute(args);
        assertEquals(ExitCode.MISSING_SCHEMA_FILE, returnCode);
    }

    @Test
    public void testValidStorageValidIndexInvalidCombination()  {
        String[] args = {"-s", "inmemory", "-i", "elasticsearch",
                         "IndexedEdgeExistenceOnSupernode"};

        final RunSubcommand runner = assertDoesNotThrow(() -> new RunSubcommand());
        CommandLine cli = new CommandLine(runner);
        int returnCode = cli.execute(args);
        assertEquals(ExitCode.INCOMPATIBLE_BACKENDS, returnCode);
    }
}
