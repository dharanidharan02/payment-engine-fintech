package com.fintech.payment_engine.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.fintech.payment_engine.model.TransactionDecision;

public class PaymentResponse {

    private UUID transactionId;
    private TransactionDecision status;
    private BigDecimal amount;
    private String currency;
    private Instant createdAt;
    private String message;

    public PaymentResponse(UUID transactionId,
                           TransactionDecision status,
                           BigDecimal amount,
                           String currency,
                           Instant createdAt,
                           String message) {

        this.transactionId = transactionId;
        this.status = status;
        this.amount = amount;
        this.currency = currency;
        this.createdAt = createdAt;
        this.message = message;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public TransactionDecision getStatus() {
        return status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getMessage() {
        return message;
    }
}