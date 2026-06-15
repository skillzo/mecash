package com.mecash.mecash_api.exception;

import com.mecash.mecash_api.dto.auth.SignupRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void signupRejectsInvalidEmail() {
        SignupRequest request = new SignupRequest();
        request.setEmail("not-an-email");
        request.setUsername("skillzo");
        request.setPassword("password123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    void transferRejectsNonPositiveAmount() {
        var request = new com.mecash.mecash_api.dto.transfer.TransferRequest();
        request.setFromAccountNumber("1234567890");
        request.setToAccountNumber("6574839201");
        request.setAmount(BigDecimal.ZERO);

        Set<ConstraintViolation<com.mecash.mecash_api.dto.transfer.TransferRequest>> violations =
                validator.validate(request);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("amount"));
    }
}
