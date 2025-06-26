package com.zerogravitysolutions.core.exceptions.database;

public class RelationshipNotFoundException extends RuntimeException{

    public RelationshipNotFoundException(final String message) {
        super(message);
    }
}
