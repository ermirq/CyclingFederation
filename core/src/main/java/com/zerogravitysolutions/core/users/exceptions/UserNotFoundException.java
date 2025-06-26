package com.zerogravitysolutions.core.users.exceptions;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(final String message) {
        super(message);
    }
}
