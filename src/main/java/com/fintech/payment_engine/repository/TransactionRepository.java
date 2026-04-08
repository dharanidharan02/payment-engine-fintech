package com.fintech.payment_engine.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fintech.payment_engine.model.Transaction;
import com.fintech.payment_engine.model.TransactionDecision;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
	
    // Optional: recent transactions (dashboard / admin / monitoring)
    List<Transaction> findTop100ByOrderByCreatedAtDesc();
    List<Transaction> findByDecisionOrderByCreatedAtDesc(TransactionDecision decision);
	
    // For fraud rule: count tx from same sender in the last X seconds
    long countBySenderAccountAndCreatedAtAfter(String senderAccount, Instant after);


}