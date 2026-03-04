package com.fintech.payment_engine.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.fintech.payment_engine.model.TransactionDecision;
import com.fintech.payment_engine.model.DeclineReason;

public class PaymentDetailsResponse {
    private UUID transactionId;
    private TransactionDecision status;
    private BigDecimal amount;
    private String currency;
    private String senderAccount;
    private String receiverAccount;
    private Instant createdAt;

    // optional only when DECLINED
    private DeclineReason declineReason;

    public PaymentDetailsResponse(UUID transactionId,
                                  TransactionDecision status,
                                  BigDecimal amount,
                                  String currency,
                                  String senderAccount,
                                  String receiverAccount,
                                  Instant createdAt,
                                  DeclineReason declineReason) {
        this.transactionId = transactionId;
        this.status = status;
        this.amount = amount;
        this.currency = currency;
        this.senderAccount = senderAccount;
        this.receiverAccount = receiverAccount;
        this.createdAt = createdAt;
        this.declineReason = declineReason;
    }

    public UUID getTransactionId() { return transactionId; }
    public TransactionDecision getStatus() { return status; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getSenderAccount() { return senderAccount; }
    public String getReceiverAccount() { return receiverAccount; }
    public Instant getCreatedAt() { return createdAt; }
    public DeclineReason getDeclineReason() { return declineReason; }
}