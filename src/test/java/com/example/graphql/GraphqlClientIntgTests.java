package com.example.graphql;

import com.example.graphql.client.LaunchListQuery;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class GraphqlClientIntgTests {

    private static final String APOLLO_SAMPLE_URI = "https://apollo-fullstack-tutorial.herokuapp.com";

    @Test
    public void Mvc_LaunchList_Cursor() throws IOException {
        GraphqlClientMvc client = new GraphqlClientMvc(
                new RestTemplateBuilder()
                        .rootUri(APOLLO_SAMPLE_URI)
                        .build(),
                new ObjectMapper());

        Optional<LaunchListQuery.Data> rsp = client.exchange(new LaunchListQuery());
        Assert.assertTrue(rsp.isPresent());
        Assert.assertNotNull(rsp.get().getLaunches());
        Assert.assertNotNull(rsp.get().getLaunches().getCursor());
    }

    @Test
    public void WebFlux_LaunchList_Cursor() throws IOException {
        GraphqlClientWebFlux client = new GraphqlClientWebFlux(
                WebClient.builder()
                        .baseUrl(APOLLO_SAMPLE_URI)
                        .build(),
                new ObjectMapper());
        Optional<LaunchListQuery.Data> rsp = client
                .exchange(new LaunchListQuery())
                .block()
                .block();
        Assert.assertNotNull(rsp);
        Assert.assertTrue(rsp.isPresent());
        Assert.assertNotNull(rsp.get().getLaunches());
        Assert.assertNotNull(rsp.get().getLaunches().getCursor());
    }
}
