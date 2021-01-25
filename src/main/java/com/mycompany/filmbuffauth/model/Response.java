package com.mycompany.filmbuffauth.model;

public class Response {

    private final Object body;

    public Response(Object body) {
        this.body = body;
    }

    public Object getBody() {
        return body;
    }
}
