package de.rngcntr.janusbench.backend;

public class InvalidConfigurationException extends IllegalArgumentException {

    private static final long serialVersionUID = 1291608002999619722L;

    public InvalidConfigurationException(String errorMessage) { super(errorMessage); }
}