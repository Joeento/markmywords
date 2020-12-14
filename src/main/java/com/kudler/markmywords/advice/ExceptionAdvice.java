package com.kudler.markmywords.advice;

import com.kudler.markmywords.response.CustomErrorResponse;
import com.kudler.markmywords.exception.BadParameterException;
import com.kudler.markmywords.exception.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(value = {FileUploadException.class})
    public ResponseEntity<CustomErrorResponse> handleUploadException(FileUploadException e) {
        return buildCustomErrorResponse(e.getClass().getSimpleName(), e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {BadParameterException.class, MultipartException.class})
    public ResponseEntity<CustomErrorResponse> handleBadParameterException(Exception e) {
        return buildCustomErrorResponse(e.getClass().getSimpleName(), e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MaxUploadSizeExceededException.class})
    public ResponseEntity<CustomErrorResponse> handleMaxSizeError(MaxUploadSizeExceededException e) {
        return buildCustomErrorResponse(e.getClass().getSimpleName(), e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomErrorResponse> handleParameterTypeMismatch(MethodArgumentTypeMismatchException e) {
        String fieldName = e.getName();
        String fieldType = e.getRequiredType().getSimpleName();
        String message = "Sorry, '" + fieldName + "' should be of type '" + fieldType + "'.";

        return buildCustomErrorResponse(e.getClass().getSimpleName(), message , HttpStatus.BAD_REQUEST);
    }

    /**
     * Catch-all to handle unforeseen exceptions with grace.
     * @param e Object containing data on the exception.
     * @return HTTP error with message in JSON body.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> exception(Exception e) {
        return buildCustomErrorResponse(e.getMessage(), e.getClass().getSimpleName(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<CustomErrorResponse> buildCustomErrorResponse(String type, String message, HttpStatus status) {
        CustomErrorResponse error = new CustomErrorResponse(type, message, status);
        return new ResponseEntity<CustomErrorResponse>(error, status);
    }
}