package com.mecash.mecash_api.domain.repository;

import com.mecash.mecash_api.domain.entity.Account;
import com.mecash.mecash_api.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    boolean existsByAccountNumber(String accountNumber);

    List<Account> findByUser(User user);

    Optional<Account> findByUserId(Long userId);
}
