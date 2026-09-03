package com.es.elsdemo.graphql;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GraphqlController {

    private final GraphqlClient client;

    @QueryMapping
    public Response fetch(@Argument Request request) {
        return client.fetch(request);
    }

    @MutationMapping
    public Response save(@Argument Request request) {
        return client.save(request);
    }

    @MutationMapping
    public Response login(@Argument Request request) {
        return client.login(request);
    }
}
