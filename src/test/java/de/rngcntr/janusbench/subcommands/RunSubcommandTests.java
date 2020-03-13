package de.rngcntr.janusbench.subcommands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.rngcntr.janusbench.util.ExitCode;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import picocli.CommandLine;

@Testcontainers
public class RunSubcommandTests {

    @Test
    public void testNoStorageNoIndex() {
        String[] args = {"EdgeExistenceOnSupernode"};

        final RunSubcommand runner = new RunSubcommand();
        CommandLine cli = new CommandLine(runner);
        int returnCode = cli.execute(args);
        assertEquals(ExitCode.INVALID_INPUT, returnCode);
    }

    @Test
    public void testInvalidStorageValidIndex() {
        String[] args = {"-s", "nonExistentStorage", "-i", "elasticsearch",
                         "EdgeExistenceOnSupernode"};

        final RunSubcommand runner = new RunSubcommand();
        CommandLine cli = new CommandLine(runner);
        int returnCode = cli.execute(args);
        assertEquals(ExitCode.INVALID_INPUT, returnCode);
    }

    @Test
    public void testInvalidStorageNoIndex() {
        String[] args = {"-s", "nonExistentStorage", "EdgeExistenceOnSupernode"};

        final RunSubcommand runner = new RunSubcommand();
        CommandLine cli = new CommandLine(runner);
        int returnCode = cli.execute(args);
        assertEquals(ExitCode.INVALID_INPUT, returnCode);
    }

    @Test
    public void testValidStorageInvalidIndex() {
        String[] args = {"-s", "cassandra", "-i", "nonExistentIndex",
                         "EdgeExistenceOnSupernode"};

        final RunSubcommand runner = new RunSubcommand();
        CommandLine cli = new CommandLine(runner);
        int returnCode = cli.execute(args);
        assertEquals(ExitCode.INVALID_INPUT, returnCode);
    }

    @Test
    public void testValidStorageInvalidSchema() {
        String[] args = {"-s", "inmemory", "--schema-script", "nonExistentSchemaScript",
                         "EdgeExistenceOnSupernode"};

        final RunSubcommand runner = new RunSubcommand();
        CommandLine cli = new CommandLine(runner);
        int returnCode = cli.execute(args);
        assertEquals(ExitCode.MISSING_SCHEMA_FILE, returnCode);
    }

    @Test
    public void testValidStorageValidIndexInvalidCombination() {
        String[] args = {"-s", "inmemory", "-i", "elasticsearch",
                         "EdgeExistenceOnSupernode"};

        final RunSubcommand runner = new RunSubcommand();
        CommandLine cli = new CommandLine(runner);
        int returnCode = cli.execute(args);
        assertEquals(ExitCode.INCOMPATIBLE_BACKENDS, returnCode);
    }
}
