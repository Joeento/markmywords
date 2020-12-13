package com.kudler.markmywords.response;

import org.springframework.http.HttpStatus;

public class CustomErrorResponse {

    String message;
    HttpStatus status;

    public CustomErrorResponse(String errorMsg) {
        super();
        this.message = errorMsg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
