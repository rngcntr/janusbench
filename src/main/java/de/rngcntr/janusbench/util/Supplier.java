package de.rngcntr.janusbench.util;

import de.rngcntr.janusbench.backend.Connection;

@FunctionalInterface
public interface Supplier<T> {
    public T get(Connection conn);
}