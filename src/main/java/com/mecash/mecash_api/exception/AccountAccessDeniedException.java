package com.mecash.mecash_api.exception;

public class AccountAccessDeniedException extends RuntimeException {

    public AccountAccessDeniedException() {
        super("You do not own the source account");
    }
}
