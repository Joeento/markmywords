package com.kudler.markmywords.response;

import org.springframework.http.HttpStatus;

public class CustomErrorResponse {
    private final String type;
    private final String message;
    private final HttpStatus status;

    public CustomErrorResponse(String type, String message, HttpStatus status) {
        super();
        this.type = type;
        this.message = message;
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }


}
