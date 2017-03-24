package com.pavel.constants;


/**
 * Перечесление возможных ответов серва клиенту
 */
public enum HttpStatus {
    STATUS_404("404 Not Found"),
    STATUS_200("200 OK"),
    STATUS_500("500 Server Error");

    private String constant;

    HttpStatus(String s) {
        constant = s;
    }

    public String getConstant() {
        return constant;
    }
}
