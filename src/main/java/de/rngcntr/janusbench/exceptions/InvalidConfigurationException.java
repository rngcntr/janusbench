package de.rngcntr.janusbench.exceptions;

import de.rngcntr.janusbench.backend.configuration.Configuration;

/**
 * This exception indicates that a {@link Configuration} was created with out-of-specification parameters.
 * 
 * @author Florian Grieskamp
 */
public class InvalidConfigurationException extends IllegalArgumentException {

    private static final long serialVersionUID = 1291608002999619722L;

    public InvalidConfigurationException() { super(); }

    public InvalidConfigurationException(String message) { super(message); }

    public InvalidConfigurationException(String message, Throwable cause) { super(message, cause); }

    public InvalidConfigurationException(Throwable cause) { super(cause); }
}