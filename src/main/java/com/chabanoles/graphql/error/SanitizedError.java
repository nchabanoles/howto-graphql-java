package com.chabanoles.graphql.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import graphql.ExceptionWhileDataFetching;

/**
 * Created by Nicolas Chabanoles on 09/08/18.
 */
public class SanitizedError extends ExceptionWhileDataFetching {

    public SanitizedError(ExceptionWhileDataFetching inner) {
        super(inner.getException());
    }

    @Override
    @JsonIgnore //Do not serialize StackTrace
    public Throwable getException() {
        return super.getException();
    }
}
