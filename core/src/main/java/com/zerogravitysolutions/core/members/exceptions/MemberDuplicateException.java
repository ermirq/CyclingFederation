package com.zerogravitysolutions.core.members.exceptions;

public class MemberDuplicateException extends RuntimeException{
    public MemberDuplicateException(final String message){
        super(message);
    }
}
