package com.networking.group.Classes;

import java.util.Map;

public class HttpRequest {
    private final String method;
    private final String url;
    private final Map<String, String> headers;
    private final String body;

    HttpRequest(String method, String url, Map<String, String> headers, String body) {
        this.method = method;
        this.url = url;
        this.headers = headers;
        this.body = body; 

    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return this.body;
    }
}
