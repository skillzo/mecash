package com.mecash.mecash_api.config;

import com.mecash.mecash_api.domain.entity.Account;
import com.mecash.mecash_api.domain.entity.User;
import com.mecash.mecash_api.domain.enums.Currency;
import com.mecash.mecash_api.domain.repository.AccountRepository;
import com.mecash.mecash_api.domain.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private static final String SEED_PASSWORD_HASH =
            "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @PostConstruct
    void seed() {
        if (accountRepository.existsByAccountNumber("1234567890")) {
            return;
        }

        seedAccount("1234567890", Currency.A, new BigDecimal("1000"));
        seedAccount("6574839201", Currency.B, new BigDecimal("1000"));
    }

    private void seedAccount(String accountNumber, Currency currency, BigDecimal balance) {
        User user = new User();
        user.setEmail(accountNumber + "@gmail.com");
        user.setUsername("user_" + accountNumber);
        user.setPassword(SEED_PASSWORD_HASH);
        user = userRepository.save(user);

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setUser(user);
        account.setCurrency(currency);
        account.setBalance(balance);
        accountRepository.save(account);
    }
}
