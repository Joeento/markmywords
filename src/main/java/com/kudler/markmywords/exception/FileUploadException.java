package com.kudler.markmywords.exception;

/**
 * Used to pass on capture and pass on information
 * about errors that occur during file upload,
 * such as read/write errors.
 */
public class FileUploadException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FileUploadException(String msg) {
        super(msg);
    }
}