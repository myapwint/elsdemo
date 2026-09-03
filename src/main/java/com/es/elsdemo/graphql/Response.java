package com.es.elsdemo.graphql;

import lombok.Data;

@Data
public class Response {

    private Status status = new Status();
    private String data;

    public static Response success(String data) {
        Response r = new Response();
        r.status.code = 200;
        r.status.message = "Success";
        r.data = data;
        return r;
    }

    public static Response error(String msg) {
        Response r = new Response();
        r.status.code = 500;
        r.status.message = msg;
        return r;
    }

    @Data
    public static class Status {
        private int code;
        private String message;
    }
}
