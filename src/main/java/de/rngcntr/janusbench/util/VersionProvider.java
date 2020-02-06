package de.rngcntr.janusbench.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import picocli.CommandLine.IVersionProvider;

public class VersionProvider implements IVersionProvider {

    private static final String[] VERSION;

    private static final Logger log = Logger.getLogger(VersionProvider.class);

    static {
        InputStream resourceAsStream =
            VersionProvider.class.getResourceAsStream("/version.properties");
        Properties props = new Properties();
        try {
            props.load(resourceAsStream);
        } catch (IOException ioex) {
            log.error(ioex);
        }
        String artifactId = props.getProperty("artifactId");
        String version = props.getProperty("version");

        VERSION = new String[] {String.format("%s v%s", artifactId, version)};
    }

    public String[] getVersion() throws Exception { return VERSION; }
}