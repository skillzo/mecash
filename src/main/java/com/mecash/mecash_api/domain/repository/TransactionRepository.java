package com.mecash.mecash_api.domain.repository;

import com.mecash.mecash_api.domain.entity.Account;
import com.mecash.mecash_api.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByFromAccountOrToAccountOrderByCreatedAtDesc(Account fromAccount, Account toAccount);
}
