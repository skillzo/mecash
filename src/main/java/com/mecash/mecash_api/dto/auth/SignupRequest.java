package com.mecash.mecash_api.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

    @NotBlank
    @Email(message = "must be a valid email address")
    private String email;

    @NotBlank
    @Size(min = 3, max = 50, message = "must be between 3 and 50 characters")
    private String username;

    @NotBlank
    @Size(min = 6, message = "must be at least 6 characters")
    private String password;
}
