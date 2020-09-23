package com.example.graphql;

@FunctionalInterface
public interface ThrowingFunctionInterface<T,R, E extends Exception> {
    R apply(T t) throws E;
}
