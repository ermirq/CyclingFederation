package com.zerogravitysolutions.core.disciplines.exceptions;

public class DisciplineNotFoundException extends RuntimeException{
    public DisciplineNotFoundException (final String message) {
        super(message);
    }
}
