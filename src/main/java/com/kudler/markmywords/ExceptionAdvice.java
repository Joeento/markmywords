package com.kudler.markmywords;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(value = {FileUploadException.class})
    public ResponseEntity<CustomErrorResponse> handleUploadException(FileUploadException e) {
        return buildCustomErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {BadParameterException.class})
    public ResponseEntity<CustomErrorResponse> handleBadParameterException(BadParameterException e) {
        return buildCustomErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<CustomErrorResponse> handleError(MaxUploadSizeExceededException e) {
        return buildCustomErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Catch-all to handle unforseen exceptions with grace.
     * @param ex Object containing data on the exception.
     * @return HTTP error with message in JSON body.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> exception(Exception e) {
        return buildCustomErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<CustomErrorResponse> buildCustomErrorResponse(String message, HttpStatus status) {
        CustomErrorResponse error = new CustomErrorResponse(message);
        error.setStatus(status);
        return new ResponseEntity<CustomErrorResponse>(error, status);
    }
}