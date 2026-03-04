package com.fintech.payment_engine.model;

public enum DeclineReason {
    LIMIT_EXCEEDED,
    SAME_SENDER_RECEIVER,
    INVALID_CURRENCY,
    INVALID_ACCOUNT_FORMAT,
    FRAUD_RULE_HIT
}