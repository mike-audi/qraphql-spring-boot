package com.example.graphql;

import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.ScalarTypeAdapters;
import okio.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;

public class GraphqlClientWebFlux {
    private final Logger logger = LoggerFactory.getLogger(GraphqlClientMvc.class);

    private final WebClient webClient;

    public GraphqlClientWebFlux(WebClient webClient) {
        this.webClient = webClient;
    }

    public <D extends Operation.Data, T, V extends Operation.Variables> Mono<Mono<T>> exchange(
            Operation<D, T, V> operation) throws IOException {
       return exchange(new GraphqlRequestBody(operation))
               .map(m -> m
                       .map(ThrowingFunctionWrapper.wrap(bytes ->
                               bytes == null ? null : operation
                                       .parse(new ByteString(bytes))
                                       .getData())));
    }

    public <D extends Operation.Data, T, V extends Operation.Variables> Mono<Mono<T>> exchange(
            Operation<D, T, V> operation,
            ScalarTypeAdapters scalarTypeAdapters) throws IOException {
        return exchange(new GraphqlRequestBody(operation))
                .map(m -> m
                        .map(ThrowingFunctionWrapper.wrap(bytes ->
                                bytes == null ? null : operation
                                        .parse(new ByteString(bytes), scalarTypeAdapters)
                                        .getData())));
    }

    private Mono<Mono<byte[]>> exchange(GraphqlRequestBody body){
        String trace =
                "Request: " + "\n" +
                "Body: " + body.toString();
        logger.trace(trace);
        return webClient
                .post()
                .bodyValue(body)
                .exchange()
                .filter(Objects::nonNull)
                .map(clientResponse -> clientResponse
                        .toEntity(byte[].class)
                        .filter(Objects::nonNull)
                        .map(responseEntity -> {
                            logger.debug(
                                    //trace + "\n" +
                                    "Response: " + "\n" +
                                    "Code: " + responseEntity.getStatusCodeValue() + "\n" +
                                    "Data: " + (responseEntity.getBody() == null ? "" : new String(responseEntity.getBody())));
                            return responseEntity.getBody();
                        })
                );

    }
}
