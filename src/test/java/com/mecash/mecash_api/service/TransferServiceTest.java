package com.mecash.mecash_api.service;

import com.mecash.mecash_api.config.ExchangeRates;
import com.mecash.mecash_api.domain.entity.Account;
import com.mecash.mecash_api.domain.entity.Transaction;
import com.mecash.mecash_api.domain.entity.User;
import com.mecash.mecash_api.domain.enums.Currency;
import com.mecash.mecash_api.domain.repository.AccountRepository;
import com.mecash.mecash_api.domain.repository.TransactionRepository;
import com.mecash.mecash_api.dto.transfer.TransferRequest;
import com.mecash.mecash_api.exception.InsufficientBalanceException;
import com.mecash.mecash_api.security.AuthPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransferService transferService;

    private User sender;
    private Account fromAccount;
    private Account toAccount;

    @BeforeEach
    void setUp() {
        sender = new User();
        sender.setId(1L);

        fromAccount = new Account();
        fromAccount.setId(10L);
        fromAccount.setAccountNumber("1234567890");
        fromAccount.setUser(sender);
        fromAccount.setCurrency(Currency.A);
        fromAccount.setBalance(new BigDecimal("1000"));

        toAccount = new Account();
        toAccount.setId(20L);
        toAccount.setAccountNumber("6574839201");
        toAccount.setCurrency(Currency.B);
        toAccount.setBalance(new BigDecimal("500"));
    }

    @Test
    void convertsCurrencyWhenTransferringFromAToB() {
        when(accountRepository.findByAccountNumber("1234567890")).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumber("6574839201")).thenReturn(Optional.of(toAccount));
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TransferRequest request = new TransferRequest();
        request.setFromAccountNumber("1234567890");
        request.setToAccountNumber("6574839201");
        request.setAmount(new BigDecimal("100"));

        var response = transferService.transfer(new AuthPrincipal(1L, "1234567890"), request);

        assertThat(response.getAmountSent()).isEqualByComparingTo("100");
        assertThat(response.getCurrencySent()).isEqualTo(Currency.A);
        assertThat(response.getCurrencyReceived()).isEqualTo(Currency.B);
        assertThat(response.getAmountReceived()).isEqualByComparingTo("134.5500");
        assertThat(response.getExchangeRateUsed()).isEqualByComparingTo(ExchangeRates.A_TO_B);

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository, times(2)).save(accountCaptor.capture());
        assertThat(accountCaptor.getAllValues().get(0).getBalance()).isEqualByComparingTo("900");
        assertThat(accountCaptor.getAllValues().get(1).getBalance()).isEqualByComparingTo("634.5500");
    }

    @Test
    void throwsWhenBalanceIsInsufficient() {
        fromAccount.setBalance(new BigDecimal("50"));
        when(accountRepository.findByAccountNumber("1234567890")).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumber("6574839201")).thenReturn(Optional.of(toAccount));

        TransferRequest request = new TransferRequest();
        request.setFromAccountNumber("1234567890");
        request.setToAccountNumber("6574839201");
        request.setAmount(new BigDecimal("100"));

        assertThatThrownBy(() -> transferService.transfer(new AuthPrincipal(1L, "1234567890"), request))
                .isInstanceOf(InsufficientBalanceException.class);
    }
}
