package com.zerogravitysolutions.core.categories.exceptions;

public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(final String message){
        super(message);
    }
}
