package com.mecash.mecash_api.security;

import lombok.Getter;

@Getter
public class AuthPrincipal {

    private final Long userId;
    private final String accountNumber;

    public AuthPrincipal(Long userId, String accountNumber) {
        this.userId = userId;
        this.accountNumber = accountNumber;
    }
}
