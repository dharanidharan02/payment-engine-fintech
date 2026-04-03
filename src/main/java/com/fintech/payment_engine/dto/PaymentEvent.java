package com.fintech.payment_engine.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class PaymentEvent {
    private UUID transactionId;
    private String decision;
    private BigDecimal amount;
    private String currency;
    private Instant createdAt;

    public PaymentEvent() {}

    public PaymentEvent(UUID transactionId, String decision, BigDecimal amount, String currency, Instant createdAt) {
        this.transactionId = transactionId;
        this.decision = decision;
        this.amount = amount;
        this.currency = currency;
        this.createdAt = createdAt;
    }

    public UUID getTransactionId() { return transactionId; }
    public void setTransactionId(UUID transactionId) { this.transactionId = transactionId; }

    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}