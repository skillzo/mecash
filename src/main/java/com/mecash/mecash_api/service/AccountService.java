package com.mecash.mecash_api.service;

import com.mecash.mecash_api.domain.entity.Account;
import com.mecash.mecash_api.domain.repository.AccountRepository;
import com.mecash.mecash_api.dto.auth.AccountResponse;
import com.mecash.mecash_api.exception.AccountNotFoundException;
import com.mecash.mecash_api.security.AuthPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountResponse getBalance(AuthPrincipal principal) {
        Account account = accountRepository.findByUserId(principal.getUserId())
                .orElseThrow(() -> new AccountNotFoundException(principal.getAccountNumber()));
        return new AccountResponse(account);
    }
}
