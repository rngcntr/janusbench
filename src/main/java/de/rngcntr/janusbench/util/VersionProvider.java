package de.rngcntr.janusbench.util;

import picocli.CommandLine.IVersionProvider;

public class VersionProvider implements IVersionProvider {

    private static final String[] VERSION = {"JanusBench v0.0.2"};

    public String[] getVersion() throws Exception {
        return VERSION;
    }
}