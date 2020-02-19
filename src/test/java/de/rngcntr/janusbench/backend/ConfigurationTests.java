package de.rngcntr.janusbench.backend;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.rngcntr.janusbench.exceptions.InvalidConfigurationException;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ConfigurationTests {

    @Test
    public void testInvalidConfigurationPathWithoutIndex() {
        assertThrows(InvalidConfigurationException.class, () -> new Configuration(null));
    }

    @Test
    public void testInvalidConfigurationPathWithIndex() {
        assertThrows(InvalidConfigurationException.class,
                     () -> new Configuration(null, Index.ELASTICSEARCH));
    }

    @Test
    public void testInvalidConfigurationPathWithIncompatibleBackends() {
        assertThrows(InvalidConfigurationException.class,
                     () -> new Configuration(Storage.INMEMORY, Index.ELASTICSEARCH));
    }

    @Test
    public void testValidConfigurationPathWithoutIndex() {
        Configuration c = new Configuration(Storage.INMEMORY);

        assertEquals("docker/configurations/janusgraph-inmemory.yml", c.getPath());
    }

    @Test
    public void testValidConfigurationPathWithIndex() {
        Configuration c = new Configuration(Storage.CASSANDRA, Index.ELASTICSEARCH);

        assertEquals("docker/configurations/janusgraph-cassandra-elasticsearch.yml", c.getPath());
    }
}