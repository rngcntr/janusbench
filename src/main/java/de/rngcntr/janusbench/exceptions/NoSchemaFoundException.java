package de.rngcntr.janusbench.exceptions;

import java.io.IOException;

/**
 * This exception indicates that the script which initializes the graph database could not be found.
 * 
 * @author Florian Grieskamp
 */
public class NoSchemaFoundException extends IOException {

    private static final long serialVersionUID = 319214637419550406L;

    public NoSchemaFoundException() { super(); }

    public NoSchemaFoundException(String message) { super(message); }

    public NoSchemaFoundException(String message, Throwable cause) { super(message, cause); }

    public NoSchemaFoundException(Throwable cause) { super(cause); }
}