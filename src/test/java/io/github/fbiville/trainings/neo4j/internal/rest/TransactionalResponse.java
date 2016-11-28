package io.github.fbiville.trainings.neo4j.internal.rest;

import java.util.Collection;

public class TransactionalResponse {

    private Collection<ResponseResult> results;
    private Collection<ResponseError> errors;

    public Collection<ResponseResult> getResults() {
        return results;
    }

    public void setResults(Collection<ResponseResult> results) {
        this.results = results;
    }

    public Collection<ResponseError> getErrors() {
        return errors;
    }

    public void setErrors(Collection<ResponseError> errors) {
        this.errors = errors;
    }
}
