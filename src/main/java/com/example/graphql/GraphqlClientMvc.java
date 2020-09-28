package com.example.graphql;

import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.ScalarTypeAdapters;
import com.fasterxml.jackson.databind.ObjectMapper;
import okio.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class GraphqlClientMvc {
    private final Logger logger = LoggerFactory.getLogger(GraphqlClientMvc.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GraphqlClientMvc(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public <D extends Operation.Data, T, V extends Operation.Variables> T exchange(
            Operation<D, T, V> operation) throws IOException {
        ResponseEntity<byte[]> responseEntity = exchange(
                GraphqlRequestBody.to(operation, objectMapper));
        if(responseEntity.getBody() != null) {
            return operation
                    .parse(new ByteString(responseEntity.getBody()))
                    .getData();
        }else return null;
    }

    public <D extends Operation.Data, T, V extends Operation.Variables> T exchange(
            Operation<D, T, V> operation,
            ScalarTypeAdapters scalarTypeAdapters) throws IOException {
        ResponseEntity<byte[]> responseEntity = exchange(
                GraphqlRequestBody.to(operation, scalarTypeAdapters, objectMapper));
        if(responseEntity.getBody() != null) {
            return operation
                    .parse(new ByteString(responseEntity.getBody()), scalarTypeAdapters)
                    .getData();
        }else return null;
    }

    private ResponseEntity<byte[]> exchange(GraphqlRequestBody body){
        String trace =
                "Request: " + "\n" +
                "POST " + restTemplate.getUriTemplateHandler().expand("/") + "\n" +
                "Body: " + body.toString();
        logger.trace(trace);
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(
                "/",
                HttpMethod.POST,
                new HttpEntity<>(body),
                byte[].class);
        logger.debug(
                trace + "\n" +
                "Response: " + "\n" +
                "Code: " + responseEntity.getStatusCodeValue() + "\n" +
                "Data: " + (responseEntity.getBody() == null ? "" : new String(responseEntity.getBody())));
        return responseEntity;
    }
}
