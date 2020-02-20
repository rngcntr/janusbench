package de.rngcntr.janusbench;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.rngcntr.janusbench.util.ExitCode;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

public class JanusBenchTests {

    @Test
    public void testNoArgs()  {
        String[] args = {};

        final JanusBench jb = new JanusBench();
        CommandLine cli = new CommandLine(jb);
        int returnCode = cli.execute(args);
        assertEquals(ExitCode.INVALID_INPUT, returnCode);
    }
}