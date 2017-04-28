package com.pavel.constants;


/**
 * ѕеречесление возможных ответов серва клиенту
 */
public enum HttpStatus {
    STATUS_404("404 Not Found"),
    STATUS_200("200 OK"),
    STATUS_500("500 Internal Server Error"),
    STATUS_501("501 Not Implemented"),
    STATUS_400("400 Bad Request"),
    STATUS_405("405 Method Not Allowed");

    private String constant;

    HttpStatus(String s) {
        constant = s;
    }

    public String getConstant() {
        return constant;
    }
}
