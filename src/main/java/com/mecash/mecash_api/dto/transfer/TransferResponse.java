package com.mecash.mecash_api.dto.transfer;

import com.mecash.mecash_api.domain.entity.Transaction;
import com.mecash.mecash_api.domain.enums.Currency;
import com.mecash.mecash_api.domain.enums.TransactionStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
public class TransferResponse {

    private final Long id;
    private final String fromAccountNumber;
    private final String toAccountNumber;
    private final BigDecimal amountSent;
    private final Currency currencySent;
    private final BigDecimal amountReceived;
    private final Currency currencyReceived;
    private final BigDecimal exchangeRateUsed;
    private final Instant timestamp;
    private final TransactionStatus status;

    public TransferResponse(Transaction transaction) {
        this.id = transaction.getId();
        this.fromAccountNumber = transaction.getFromAccount().getAccountNumber();
        this.toAccountNumber = transaction.getToAccount().getAccountNumber();
        this.amountSent = transaction.getAmountSent();
        this.currencySent = transaction.getCurrencySent();
        this.amountReceived = transaction.getAmountReceived();
        this.currencyReceived = transaction.getCurrencyReceived();
        this.exchangeRateUsed = transaction.getExchangeRateUsed();
        this.timestamp = transaction.getTimestamp();
        this.status = transaction.getStatus();
    }
}
