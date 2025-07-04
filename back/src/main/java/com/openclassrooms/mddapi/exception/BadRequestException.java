package com.openclassrooms.mddapi.exception;

/**
 * Exception thrown when a client provides a malformed or invalid request.
 * This corresponds to a 400 Bad Request HTTP status.
 */
public class BadRequestException extends RuntimeException {
    /**
     * Constructs a new BadRequestException with the specified detail message.
     *
     * @param message the detail message.
     */
    public BadRequestException(String message) {
        super(message);
    }
}