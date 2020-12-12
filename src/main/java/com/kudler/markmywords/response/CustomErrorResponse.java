package com.kudler.markmywords.response;

import org.springframework.http.HttpStatus;

public class CustomErrorResponse {

    String errorMsg;
    HttpStatus status;

    public CustomErrorResponse(String errorMsg) {
        super();
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
