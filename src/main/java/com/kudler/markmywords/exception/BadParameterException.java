package com.kudler.markmywords.exception;

/**
 * Used to pass on capture and pass on information
 * about API endpoint parameters that have been
 * incorrectly formatted or missing.
 */
public class BadParameterException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BadParameterException(String msg) {
        super(msg);
    }
}