package com.mecash.mecash_api.dto.auth;

import com.mecash.mecash_api.domain.entity.User;
import lombok.Getter;

import java.time.Instant;

@Getter
public class UserResponse {

    private final Long id;
    private final String email;
    private final String username;
    private final Instant createdAt;

    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.createdAt = user.getCreatedAt();
    }
}
