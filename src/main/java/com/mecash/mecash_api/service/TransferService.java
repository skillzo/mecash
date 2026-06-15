package com.mecash.mecash_api.service;

import com.mecash.mecash_api.config.ExchangeRates;
import com.mecash.mecash_api.domain.entity.Account;
import com.mecash.mecash_api.domain.entity.Transaction;
import com.mecash.mecash_api.domain.enums.Currency;
import com.mecash.mecash_api.domain.enums.TransactionStatus;
import com.mecash.mecash_api.domain.repository.AccountRepository;
import com.mecash.mecash_api.domain.repository.TransactionRepository;
import com.mecash.mecash_api.dto.transfer.TransferRequest;
import com.mecash.mecash_api.dto.transfer.TransferResponse;
import com.mecash.mecash_api.exception.AccountAccessDeniedException;
import com.mecash.mecash_api.exception.AccountNotFoundException;
import com.mecash.mecash_api.exception.InsufficientBalanceException;
import com.mecash.mecash_api.security.AuthPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class TransferService {

    private static final int AMOUNT_SCALE = 4;
    private static final int RATE_SCALE = 8;

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public TransferResponse transfer(AuthPrincipal principal, TransferRequest request) {
        Account fromAccount = accountRepository.findByAccountNumber(request.getFromAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException(request.getFromAccountNumber()));

        if (!fromAccount.getUser().getId().equals(principal.getUserId())) {
            throw new AccountAccessDeniedException();
        }

        Account toAccount = accountRepository.findByAccountNumber(request.getToAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException(request.getToAccountNumber()));

        BigDecimal amountSent = request.getAmount().setScale(AMOUNT_SCALE, RoundingMode.HALF_UP);

        if (fromAccount.getBalance().compareTo(amountSent) < 0) {
            throw new InsufficientBalanceException();
        }

        Currency currencySent = fromAccount.getCurrency();
        Currency currencyReceived = toAccount.getCurrency();
        BigDecimal exchangeRateUsed = resolveExchangeRate(currencySent, currencyReceived);
        BigDecimal amountReceived = amountSent.multiply(exchangeRateUsed)
                .setScale(AMOUNT_SCALE, RoundingMode.HALF_UP);

        fromAccount.setBalance(fromAccount.getBalance().subtract(amountSent));
        toAccount.setBalance(toAccount.getBalance().add(amountReceived));
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmountSent(amountSent);
        transaction.setCurrencySent(currencySent);
        transaction.setAmountReceived(amountReceived);
        transaction.setCurrencyReceived(currencyReceived);
        transaction.setExchangeRateUsed(exchangeRateUsed);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction = transactionRepository.save(transaction);

        return new TransferResponse(transaction);
    }

    private BigDecimal resolveExchangeRate(Currency from, Currency to) {
        if (from == to) {
            return BigDecimal.ONE.setScale(RATE_SCALE, RoundingMode.HALF_UP);
        }
        if (from == Currency.A) {
            return ExchangeRates.A_TO_B;
        }
        return ExchangeRates.B_TO_A;
    }
}
