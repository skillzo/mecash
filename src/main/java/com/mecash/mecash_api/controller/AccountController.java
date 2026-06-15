package com.mecash.mecash_api.controller;

import com.mecash.mecash_api.dto.auth.AccountResponse;
import com.mecash.mecash_api.security.AuthPrincipal;
import com.mecash.mecash_api.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/balance")
    public AccountResponse getBalance(@AuthenticationPrincipal AuthPrincipal principal) {
        return accountService.getBalance(principal);
    }
}
