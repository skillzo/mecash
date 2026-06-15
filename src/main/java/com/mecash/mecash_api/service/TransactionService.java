package com.mecash.mecash_api.service;

import com.mecash.mecash_api.domain.entity.Account;
import com.mecash.mecash_api.domain.entity.Transaction;
import com.mecash.mecash_api.domain.repository.AccountRepository;
import com.mecash.mecash_api.domain.repository.TransactionRepository;
import com.mecash.mecash_api.dto.transfer.TransferResponse;
import com.mecash.mecash_api.exception.AccountNotFoundException;
import com.mecash.mecash_api.security.AuthPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public List<TransferResponse> getHistory(AuthPrincipal principal) {
        Account account = accountRepository.findByUserId(principal.getUserId())
                .orElseThrow(() -> new AccountNotFoundException(principal.getAccountNumber()));

        return transactionRepository
                .findByFromAccountOrToAccountOrderByTimestampDesc(account, account)
                .stream()
                .map(TransferResponse::new)
                .toList();
    }
}
