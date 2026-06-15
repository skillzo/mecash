package com.mecash.mecash_api.controller;

import com.mecash.mecash_api.dto.transfer.TransferResponse;
import com.mecash.mecash_api.security.AuthPrincipal;
import com.mecash.mecash_api.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/history")
    public List<TransferResponse> getHistory(@AuthenticationPrincipal AuthPrincipal principal) {
        return transactionService.getHistory(principal);
    }
}
