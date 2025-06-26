package com.zerogravitysolutions.core.exceptions;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

public record ErrorResponse(
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path,
    Map<String, Object> additionalDetails
) {
    public static ErrorResponse from(
        final HttpStatus status,
        final String message,
        final WebRequest request,
        final Map<String, Object> additionalDetails
    ) {
        return new ErrorResponse(
            LocalDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            message,
            ((ServletWebRequest) request).getRequest().getRequestURI(),
            additionalDetails
        );
    }
}