package com.example.graphql;

import java.util.function.Function;

public class ThrowingFunctionWrapper {
    static<T,R> Function<T,R> wrap(
            ThrowingFunctionInterface<T,R, Exception> throwingFunction) {
        return i -> {
            try {
                return throwingFunction.apply(i);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }
}
