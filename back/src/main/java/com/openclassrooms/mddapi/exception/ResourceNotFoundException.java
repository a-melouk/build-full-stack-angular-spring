package com.openclassrooms.mddapi.exception;

/**
 * Exception thrown when a requested resource cannot be found in the system.
 * This corresponds to a 404 Not Found HTTP status.
 */
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     *
     * @param message the detail message.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}