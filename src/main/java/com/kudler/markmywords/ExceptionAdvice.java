package com.kudler.markmywords;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

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

    public ResponseEntity<CustomErrorResponse> buildCustomErrorResponse(String message, HttpStatus status) {
        CustomErrorResponse error = new CustomErrorResponse(message);
        error.setStatus(status);
        return new ResponseEntity<CustomErrorResponse>(error, status);
    }
}