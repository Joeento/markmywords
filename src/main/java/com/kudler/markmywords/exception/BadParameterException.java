package com.kudler.markmywords.exception;

public class BadParameterException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BadParameterException(String parameter, String type) {
        super("Sorry, one of your parameters was invalid.  Please make sure you have a '" + parameter + "' field of type '" + type + "'.");
    }
}