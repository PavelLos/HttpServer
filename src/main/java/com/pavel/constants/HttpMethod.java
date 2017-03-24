package com.pavel.constants;

/**
 * Перечесление возможных запросов для сервера.
 */
public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    HEAD("HEAD");

    private String method;

    HttpMethod(String string) {
        method = string;
    }

    public String getMethod() {
        return method;
    }

}
