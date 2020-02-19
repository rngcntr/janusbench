package de.rngcntr.janusbench.exceptions;

import java.io.IOException;

public class NoSchemaFoundException extends IOException {

    private static final long serialVersionUID = 319214637419550406L;

    public NoSchemaFoundException(String errorMessage) { super(errorMessage); }
}