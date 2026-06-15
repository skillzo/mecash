package com.mecash.mecash_api.controller;

import com.mecash.mecash_api.dto.transfer.TransferRequest;
import com.mecash.mecash_api.dto.transfer.TransferResponse;
import com.mecash.mecash_api.security.AuthPrincipal;
import com.mecash.mecash_api.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public TransferResponse transfer(
            @AuthenticationPrincipal AuthPrincipal principal,
            @Valid @RequestBody TransferRequest request) {
        return transferService.transfer(principal, request);
    }
}
