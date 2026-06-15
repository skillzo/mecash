package com.mecash.mecash_api.exception;

import lombok.Getter;

import java.time.Instant;

@Getter
public class ApiErrorResponse {

    private final String timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;

    public ApiErrorResponse(int status, String error, String message, String path) {
        this.timestamp = Instant.now().toString();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
