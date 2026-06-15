package com.mecash.mecash_api.exception;

public class DuplicateUsernameException extends RuntimeException {

    public DuplicateUsernameException(String username) {
        super("Username already in use: " + username);
    }
}
