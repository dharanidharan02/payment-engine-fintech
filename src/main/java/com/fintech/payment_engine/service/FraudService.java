package com.fintech.payment_engine.service;

import org.springframework.stereotype.Service;
import java.time.Instant;
import com.fintech.payment_engine.dto.PaymentRequest;
import com.fintech.payment_engine.repository.TransactionRepository;

import com.fintech.payment_engine.dto.PaymentRequest;
import com.fintech.payment_engine.repository.TransactionRepository;

import java.time.Instant;

@Service
public class FraudService {

    private final TransactionRepository transactionRepository;

    public FraudService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public boolean isSuspicious(PaymentRequest req) {
        // Rule 1: High amount
    	if (req.getAmount().compareTo(new java.math.BigDecimal("10000.00")) >= 0) return true;

        // Rule 2: Too many transactions in 60 seconds
        Instant oneMinuteAgo = Instant.now().minusSeconds(60);
        long recentCount = transactionRepository.countBySenderAccountAndCreatedAtAfter(req.getSenderAccount(), oneMinuteAgo);
        return recentCount >= 3;
    }
}