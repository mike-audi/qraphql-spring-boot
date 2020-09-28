package com.example.graphql;

import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.ScalarTypeAdapters;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class GraphqlRequestBody {
    private String operationName;
    private String query;
    private Map<String, ?> variables;

    public GraphqlRequestBody() {
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

    public Map<String, ?> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, ?> variables) {
        this.variables = variables;
    }

    public static <D extends Operation.Data, T, V extends Operation.Variables> GraphqlRequestBody to(
            Operation<D, T, V> operation,
            ObjectMapper objectMapper) throws IOException {
        return to(operation, null, objectMapper);
    }

    public static <D extends Operation.Data, T, V extends Operation.Variables> GraphqlRequestBody to(
            Operation<D, T, V> operation,
            ScalarTypeAdapters scalarTypeAdapters,
            ObjectMapper objectMapper) throws IOException {
        GraphqlRequestBody body = new GraphqlRequestBody();
        body.setOperationName(operation.name().name());
        body.setQuery(operation.queryDocument());
        TypeReference<Map<String, ?>> typeReference = new TypeReference<Map<String, ?>>() {};
        body.setVariables(scalarTypeAdapters != null ?
                objectMapper.readValue(operation.variables().marshal(scalarTypeAdapters), typeReference) :
                objectMapper.readValue(operation.variables().marshal(), typeReference));
        return body;
    }
}
