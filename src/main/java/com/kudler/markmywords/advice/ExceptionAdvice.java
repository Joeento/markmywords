package com.kudler.markmywords.advice;

import com.kudler.markmywords.response.CustomErrorResponse;
import com.kudler.markmywords.exception.BadParameterException;
import com.kudler.markmywords.exception.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(value = {FileUploadException.class})
    public ResponseEntity<CustomErrorResponse> handleUploadException(FileUploadException e) {
        return buildCustomErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {BadParameterException.class})
    public ResponseEntity<CustomErrorResponse> handleBadParameterException(BadParameterException e) {
        return buildCustomErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<CustomErrorResponse> handleError(MaxUploadSizeExceededException e) {
        return buildCustomErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    /**
     * Catch-all to handle unforeseen exceptions with grace.
     * @param e Object containing data on the exception.
     * @return HTTP error with message in JSON body.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> exception(Exception e) {
        return buildCustomErrorResponse(e, HttpStatus.BAD_GATEWAY);
    }

    public ResponseEntity<CustomErrorResponse> buildCustomErrorResponse(Exception ex, HttpStatus status) {
        CustomErrorResponse error = new CustomErrorResponse(ex.getClass().getSimpleName(), ex.getMessage());
        error.setStatus(status);
        return new ResponseEntity<CustomErrorResponse>(error, status);
    }
}