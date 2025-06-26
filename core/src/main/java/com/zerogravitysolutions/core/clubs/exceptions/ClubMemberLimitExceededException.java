package com.zerogravitysolutions.core.clubs.exceptions;

public class ClubMemberLimitExceededException extends RuntimeException{

    public ClubMemberLimitExceededException(final String message) {
        super(message);
    }
}
