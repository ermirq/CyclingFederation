package com.zerogravitysolutions.core.audit.exceptions;

public class InvalidAuditDocumentException extends RuntimeException {
    public InvalidAuditDocumentException(final String message){
        super(message);
    }
}
