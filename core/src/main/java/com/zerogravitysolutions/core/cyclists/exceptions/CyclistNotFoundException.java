package com.zerogravitysolutions.core.cyclists.exceptions;

public class CyclistNotFoundException extends RuntimeException {
    public CyclistNotFoundException(final String message) {
        super(message);
    }
}
