package com.mecash.mecash_api.dto.auth;

import com.mecash.mecash_api.domain.entity.Account;
import com.mecash.mecash_api.domain.enums.Currency;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class AccountResponse {

    private final String accountNumber;
    private final Currency currency;
    private final BigDecimal balance;

    public AccountResponse(Account account) {
        this.accountNumber = account.getAccountNumber();
        this.currency = account.getCurrency();
        this.balance = account.getBalance();
    }
}
