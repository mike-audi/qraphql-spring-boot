package com.example.graphql;

import com.example.graphql.client.LaunchListQuery;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.io.IOException;
import java.util.Optional;

public class GraphqlClientIntgTests {

    private static final String APOLLO_SAMPLE_URI = "https://apollo-fullstack-tutorial.herokuapp.com";

    @Test
    public void Query_LaunchList_Cursor() throws IOException {
        GraphqlClient client = new GraphqlClient(
                new RestTemplateBuilder()
                        .rootUri(APOLLO_SAMPLE_URI)
                        .build());

        Optional<LaunchListQuery.Data> rsp = client.exchange(new LaunchListQuery());
        Assert.assertTrue(rsp.isPresent());
        Assert.assertNotNull(rsp.get().getLaunches());
        Assert.assertNotNull(rsp.get().getLaunches().getCursor());
    }
}
