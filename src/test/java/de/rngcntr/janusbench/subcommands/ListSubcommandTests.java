package de.rngcntr.janusbench.subcommands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.rngcntr.janusbench.backend.Index;
import de.rngcntr.janusbench.backend.Storage;
import de.rngcntr.janusbench.util.ExitCode;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

public class ListSubcommandTests {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testNoArgs()  {
        String[] args = {};

        final ListSubcommand ls = new ListSubcommand();
        CommandLine cli = new CommandLine(ls);
        int returnCode = cli.execute(args);
        assertEquals(ExitCode.INVALID_INPUT, returnCode);
    }

    @Test
    public void testInvalidArgs()  {
        String[] args = {"invalidListArgument"};

        final ListSubcommand ls = new ListSubcommand();
        CommandLine cli = new CommandLine(ls);
        int returnCode = cli.execute(args);
        assertEquals(ExitCode.INVALID_INPUT, returnCode);
    }

    @Test
    public void testListStorage()  {
        String[] args = {"storage"};

        final ListSubcommand ls = new ListSubcommand();
        CommandLine cli = new CommandLine(ls);
        int returnCode = cli.execute(args);
        assertEquals(ExitCode.OK, returnCode);
        String outputString = outContent.toString();

        for (Storage s : Storage.values()) {
            assertTrue(outputString.contains(s.toString()),
                       s.toString() + " is expected to be returnd by 'janusbench list storage'");
        }
    }

    @Test
    public void testListIndexes()  {
        String[] args = {"index"};

        final ListSubcommand ls = new ListSubcommand();
        CommandLine cli = new CommandLine(ls);
        int returnCode = cli.execute(args);
        assertEquals(ExitCode.OK, returnCode);
        String outputString = outContent.toString();

        for (Index i : Index.values()) {
            assertTrue(outputString.contains(i.toString()),
                       i.toString() + " is expected to be returnd by 'janusbench list index'");
        }
    }

    @Test
    public void testListBenchmark()  {
        String[] args = {"benchmark"};

        final ListSubcommand ls = new ListSubcommand();
        CommandLine cli = new CommandLine(ls);
        int returnCode = cli.execute(args);
        assertEquals(ExitCode.OK, returnCode);
        String outputString = outContent.toString();

        assertTrue(
            outputString.contains("EdgeExistenceOnSupernode"),
            "EdgeExistenceOnSupernode is expected to be returnd by 'janusbench list storage'");
    }
}