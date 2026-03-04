package com.fintech.payment_engine.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String senderAccount;

    @Column(nullable = false)
    private String receiverAccount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    // FinTech: Decision is what you return to clients
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionDecision decision;

    // Optional, only if declined
    @Enumerated(EnumType.STRING)
    private DeclineReason declineReason;

    @Column(nullable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = Instant.now();
    }

    public UUID getId() { return id; }

    public String getSenderAccount() { return senderAccount; }
    public void setSenderAccount(String senderAccount) { this.senderAccount = senderAccount; }

    public String getReceiverAccount() { return receiverAccount; }
    public void setReceiverAccount(String receiverAccount) { this.receiverAccount = receiverAccount; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public TransactionDecision getDecision() { return decision; }
    public void setDecision(TransactionDecision decision) { this.decision = decision; }

    public DeclineReason getDeclineReason() { return declineReason; }
    public void setDeclineReason(DeclineReason declineReason) { this.declineReason = declineReason; }

    public Instant getCreatedAt() { return createdAt; }
}