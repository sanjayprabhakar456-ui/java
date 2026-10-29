package com.campusnav.exception;

/**
 * Custom Exception thrown when a campus location cannot be found.
 * Demonstrates Java Exception hierarchy and custom exception handling.
 */
public class LocationNotFoundException extends Exception {
    public LocationNotFoundException(String message) {
        super(message);
    }
}
