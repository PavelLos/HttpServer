package com.pavel.constants;

/**
 * ������������ ��������� �������� ��� �������.
 */
public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    HEAD("HEAD"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    TRACE("TRACE"),
    CONNECT("CONNECT"),
    OPTIONS("OPTIONS");

    private String method;

    HttpMethod(String string) {
        method = string;
    }

    public String getMethod() {
        return method;
    }

}
