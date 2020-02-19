package de.rngcntr.janusbench.exceptions;

public class InvalidConfigurationException extends IllegalArgumentException {

    private static final long serialVersionUID = 1291608002999619722L;

    public InvalidConfigurationException() { super(); }

    public InvalidConfigurationException(String message) { super(message); }

    public InvalidConfigurationException(String message, Throwable cause) { super(message, cause); }

    public InvalidConfigurationException(Throwable cause) { super(cause); }
}