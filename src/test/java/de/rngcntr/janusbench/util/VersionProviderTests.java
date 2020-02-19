package de.rngcntr.janusbench.util;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class VersionProviderTests {

    @Test
    public void testVersionNotEmpty() {
        try {
            String[] version = new VersionProvider().getVersion();
            assertNotNull(version, "The version must not be null");
            assertNotEquals(0, version.length, "The version must contain at least one string");
            assertTrue(Arrays.stream(version).allMatch(s -> s != null),
                       "None of the version strings must be null");
            assertTrue(Arrays.stream(version).anyMatch(s -> s.length() > 0),
                       "At least one of the version strings must be non empty");
        } catch (Exception ex) {
            fail("Getting version should not cause exceptions");
        }
    }
}