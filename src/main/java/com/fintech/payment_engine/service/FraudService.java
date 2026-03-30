package com.fintech.payment_engine.service;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.stereotype.Service;

import com.fintech.payment_engine.dto.PaymentRequest;
import com.fintech.payment_engine.repository.TransactionRepository;


@Service
public class FraudService {

    private final TransactionRepository transactionRepository;

    public FraudService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public boolean isSuspicious(PaymentRequest req) {
        if (req.getAmount() == null) {
            return false;
        }

        if (req.getAmount().compareTo(new BigDecimal("10000.00")) >= 0) {
            return true;
        }

        String sender = req.getSenderAccount() == null ? null : req.getSenderAccount().trim();
        if (sender == null || sender.isBlank()) {
            return false;
        }

        Instant oneMinuteAgo = Instant.now().minusSeconds(60);
        long recentCount = transactionRepository.countBySenderAccountAndCreatedAtAfter(sender, oneMinuteAgo);

        return recentCount >= 3;
    }

}