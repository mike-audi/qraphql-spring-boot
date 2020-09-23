package com.example.graphql;

import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.ScalarTypeAdapters;

import java.io.IOException;

public class GraphqlRequestBody {
    private String operationName;
    private String query;
    private String variables;

    public <D extends Operation.Data,T,V extends Operation.Variables> GraphqlRequestBody(
            Operation<D,T,V> operation) throws IOException {
        this.operationName = operation.name().name();
        this.query = operation.queryDocument();
        this.variables = operation.variables().marshal();
    }

    public <D extends Operation.Data,T,V extends Operation.Variables> GraphqlRequestBody(
            Operation<D,T,V> operation,
            ScalarTypeAdapters scalarTypeAdapters) throws IOException {
        this.operationName = operation.name().name();
        this.query = operation.queryDocument();
        this.variables = operation.variables().marshal(scalarTypeAdapters);
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getVariables() {
        return variables;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        return "{" +
                "\"operationName\": \"" + operationName + "\", " +
                "\"query\": \"" + query + "\", " +
                "\"variables\": \"" + variables +
                "\"}";
    }
}
