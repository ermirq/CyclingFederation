package com.zerogravitysolutions.core.members.exceptions;

public class MemberNotFoundException extends RuntimeException{
    public MemberNotFoundException(final String message){
        super(message);
    }
}
