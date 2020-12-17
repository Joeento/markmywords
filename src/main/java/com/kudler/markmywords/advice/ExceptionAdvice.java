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

/**
 * Advice class to take over control of the application
 * when an error is thrown in any other class.
 * Instead of letting random methods catch the exceptions,
 * we manage each one and have it return a custom
 * JSON response to the user.
 */
@RestControllerAdvice
public class ExceptionAdvice {
    /**
     * If a user incorrectly uploads a file(e.g. sends a file
     * with the wrong extension), throw an exception and return a
     * 400 response.
     * @param e Instance of exception, usually includes a
     *          message that will be shown to the user so they understand the mistake.
     * @return JSON Response with the exception name and an explanation of the problem.
     */
    @ExceptionHandler(value = {FileUploadException.class})
    public ResponseEntity<CustomErrorResponse> handleUploadException(FileUploadException e) {
        return buildCustomErrorResponse(e.getClass().getSimpleName(), e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * If parameters in the POST body are not acceptable
     * (e.g. "file" is missing, prefix is longer than the text, etc.)
     * @param e Instance of exception, usually includes a
     *          message that will be shown to the user so they understand the mistake.
     * @return JSON Response with the exception name and an explanation of the problem.
     */
    @ExceptionHandler(value = {BadParameterException.class, MultipartException.class})
    public ResponseEntity<CustomErrorResponse> handleBadParameterException(Exception e) {
        return buildCustomErrorResponse(e.getClass().getSimpleName(), e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Spring throws MaxUploadSizeExceededException when
     * file size is larger than spring.servlet.multipart.max-file-size.
     * This method catches it and displays an warning in a user friendly response.
     * @param e Instance of exception, usually includes a
     *          message that will be shown to the user so they understand the mistake.
     * @return JSON Response with the exception name and an explanation of the problem.
     */
    @ExceptionHandler(value = {MaxUploadSizeExceededException.class})
    public ResponseEntity<CustomErrorResponse> handleMaxSizeError(MaxUploadSizeExceededException e) {
        return buildCustomErrorResponse(e.getClass().getSimpleName(), e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Spring throws MethodArgumentTypeMismatchException when a POST
     * parameter cannot be parsed into it's declared type.  This method
     * catches it and displays an warning in a user friendly response.
     * @param e Instance of exception, usually includes a
     *          message that will be shown to the user so they understand the mistake.
     * @return JSON Response with the exception name and an explanation of the problem.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomErrorResponse> handleParameterTypeMismatch(MethodArgumentTypeMismatchException e) {
        String fieldName = e.getName();
        String message;
        if (e.getRequiredType() == null) {
            message = "Sorry, '" + fieldName + "' is an incorrect type.";
        } else {
            String fieldType = e.getRequiredType().getSimpleName();
            message = "Sorry, '" + fieldName + "' should be of type '" + fieldType + "'.";
        }

        return buildCustomErrorResponse(e.getClass().getSimpleName(), message , HttpStatus.BAD_REQUEST);
    }

    /**
     * Catch-all to handle unforeseen exceptions with grace.
     * @param e Instance of exception, usually includes a
     *          message that will be shown to the user so they understand the mistake.
     * @return JSON Response with the exception name and an explanation of the problem.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> exception(Exception e) {
        return buildCustomErrorResponse(e.getMessage(), e.getClass().getSimpleName(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Helper method for taking information about a given
     * exception and turning it into an easily readable JSON
     * with customizable HTTP Status code.
     * @param type Info on the exception typically the class name
     * @param message An easy to understand summary of what went wrong and how to fix it
     * @param status An HTTP Response Code to indicate something went wrong,
     *               typically something from teh 400s(client error)
     * @return An HTTP Response containing all of the data to include in the JSON
     */
    public ResponseEntity<CustomErrorResponse> buildCustomErrorResponse(String type, String message, HttpStatus status) {
        CustomErrorResponse error = new CustomErrorResponse(type, message, status);
        return new ResponseEntity<>(error, status);
    }
}