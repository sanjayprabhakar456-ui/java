package com.campusnav.exception;

/**
 * Custom Exception thrown when authentication fails.
 * Demonstrates Java Exception hierarchy and custom exception handling.
 */
public class InvalidLoginException extends Exception {
    public InvalidLoginException(String message) {
        super(message);
    }
}
