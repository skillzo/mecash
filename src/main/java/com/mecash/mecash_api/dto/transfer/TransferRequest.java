package com.mecash.mecash_api.dto.transfer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferRequest {

    @NotBlank
    @Pattern(regexp = "\\d{10}", message = "must be a 10-digit account number")
    private String fromAccountNumber;

    @NotBlank
    @Pattern(regexp = "\\d{10}", message = "must be a 10-digit account number")
    private String toAccountNumber;

    @NotNull(message = "amount is required")
    @Positive(message = "must be greater than 0")
    private BigDecimal amount;
}
