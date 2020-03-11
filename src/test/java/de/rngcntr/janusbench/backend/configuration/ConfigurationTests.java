package de.rngcntr.janusbench.backend.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.rngcntr.janusbench.backend.Index;
import de.rngcntr.janusbench.backend.Storage;
import de.rngcntr.janusbench.exceptions.InvalidConfigurationException;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ConfigurationTests {

    @Test
    public void testInvalidConfigurationPathWithoutIndex() {
        assertThrows(InvalidConfigurationException.class, () -> new ComposeConfiguration(null));
    }

    @Test
    public void testInvalidConfigurationPathWithIndex() {
        assertThrows(InvalidConfigurationException.class,
                     () -> new ComposeConfiguration(null, Index.ELASTICSEARCH));
    }

    @Test
    public void testInvalidConfigurationPathWithIncompatibleBackends() {
        assertThrows(InvalidConfigurationException.class,
                     () -> new ComposeConfiguration(Storage.INMEMORY, Index.ELASTICSEARCH));
    }

    @Test
    public void testValidConfigurationPathWithoutIndex() {
        Configuration c = new ComposeConfiguration(Storage.INMEMORY);

        assertEquals("docker/configurations/janusgraph-inmemory.yml", c.getPath());
    }

    @Test
    public void testValidConfigurationPathWithIndex() {
        Configuration c = new ComposeConfiguration(Storage.CASSANDRA, Index.ELASTICSEARCH);

        assertEquals("docker/configurations/janusgraph-cassandra-elasticsearch.yml", c.getPath());
    }
}