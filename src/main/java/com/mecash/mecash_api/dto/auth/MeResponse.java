package com.mecash.mecash_api.dto.auth;

import lombok.Getter;

@Getter
public class MeResponse {

    private final UserResponse user;
    private final AccountResponse account;

    public MeResponse(UserResponse user, AccountResponse account) {
        this.user = user;
        this.account = account;
    }
}
