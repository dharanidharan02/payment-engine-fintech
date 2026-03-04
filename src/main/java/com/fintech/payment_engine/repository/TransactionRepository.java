package com.fintech.payment_engine.repository;

import com.fintech.payment_engine.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
	
    // Optional: recent transactions (dashboard / admin / monitoring)
    List<Transaction> findTop100ByOrderByCreatedAtDesc();
	
    // For fraud rule: count tx from same sender in the last X seconds
    long countBySenderAccountAndCreatedAtAfter(String senderAccount, Instant after);


}