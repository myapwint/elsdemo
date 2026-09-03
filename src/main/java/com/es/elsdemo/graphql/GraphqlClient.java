package com.es.elsdemo.graphql;

import com.es.elsdemo.standard.StandardHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GraphqlClient {

    private final StandardHandler handler;

    public Response fetch(Request request) {
        return handler.fetch(request);
    }

    public Response save(Request request) {
        return handler.save(request);
    }


    public Response login(Request request) {
        return handler.save(request);
    }
}
