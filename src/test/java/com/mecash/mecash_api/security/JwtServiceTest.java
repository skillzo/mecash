package com.mecash.mecash_api.security;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService(
            UUID.randomUUID().toString(),
            86400000);

    @Test
    void generateAndParseToken() {
        String token = jwtService.generateToken(1L, "1234567890");

        var principal = jwtService.parseToken(token);

        assertThat(principal).isPresent();
        assertThat(principal.get().getUserId()).isEqualTo(1L);
        assertThat(principal.get().getAccountNumber()).isEqualTo("1234567890");
    }

    @Test
    void parseInvalidTokenReturnsEmpty() {
        assertThat(jwtService.parseToken("invalid.token.value")).isEmpty();
    }
}
