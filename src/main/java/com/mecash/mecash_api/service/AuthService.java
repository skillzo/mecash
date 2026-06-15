package com.mecash.mecash_api.service;

import com.mecash.mecash_api.domain.entity.Account;
import com.mecash.mecash_api.domain.entity.User;
import com.mecash.mecash_api.domain.enums.Currency;
import com.mecash.mecash_api.domain.repository.AccountRepository;
import com.mecash.mecash_api.domain.repository.UserRepository;
import com.mecash.mecash_api.dto.auth.AccountResponse;
import com.mecash.mecash_api.dto.auth.LoginRequest;
import com.mecash.mecash_api.dto.auth.LoginResponse;
import com.mecash.mecash_api.dto.auth.MeResponse;
import com.mecash.mecash_api.dto.auth.SignupRequest;
import com.mecash.mecash_api.dto.auth.SignupResponse;
import com.mecash.mecash_api.dto.auth.UserResponse;
import com.mecash.mecash_api.exception.DuplicateEmailException;
import com.mecash.mecash_api.exception.DuplicateUsernameException;
import com.mecash.mecash_api.exception.InvalidCredentialsException;
import com.mecash.mecash_api.exception.UserNotFoundException;
import com.mecash.mecash_api.security.AuthPrincipal;
import com.mecash.mecash_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final BigDecimal INITIAL_BALANCE = new BigDecimal("1000");

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateUsernameException(request.getUsername());
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user = userRepository.save(user);

        Account account = new Account();
        account.setUser(user);
        account.setAccountNumber(generateUniqueAccountNumber());
        account.setCurrency(secureRandom.nextBoolean() ? Currency.A : Currency.B);
        account.setBalance(INITIAL_BALANCE);
        account = accountRepository.save(account);

        return new SignupResponse(new UserResponse(user), new AccountResponse(account));
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        Account account = accountRepository.findByUserId(user.getId())
                .orElseThrow(InvalidCredentialsException::new);

        String token = jwtService.generateToken(user.getId(), account.getAccountNumber());
        return new LoginResponse(token, new UserResponse(user));
    }

    public MeResponse getMe(AuthPrincipal principal) {
        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new UserNotFoundException(principal.getUserId()));

        Account account = accountRepository.findByUserId(principal.getUserId())
                .orElseThrow(InvalidCredentialsException::new);

        return new MeResponse(new UserResponse(user), new AccountResponse(account));
    }

    private String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = String.format("%010d", secureRandom.nextInt(1_000_000_000));
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }
}
