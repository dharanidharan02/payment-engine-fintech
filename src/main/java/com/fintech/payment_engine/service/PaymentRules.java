package com.fintech.payment_engine.service;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.fintech.payment_engine.dto.PaymentRequest;
import com.fintech.payment_engine.model.DeclineReason;

@Component
public class PaymentRules {

    private static final BigDecimal MAX_AMOUNT = new BigDecimal("10000.00");
    private static final Set<String> ALLOWED_CURRENCIES = Set.of("USD", "CAD");

    public DeclineReason evaluateDeclineReason(PaymentRequest req) {

        if (req.getAmount() == null || req.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return DeclineReason.INVALID_AMOUNT; // or create INVALID_AMOUNT if you want
            
        }

//        if (req.getAmount().compareTo(MAX_AMOUNT) > 0) {
//            return DeclineReason.LIMIT_EXCEEDED;
//        }

//        if (req.getSenderAccount() != null && req.getSenderAccount().equals(req.getReceiverAccount())) {
//            return DeclineReason.SAME_SENDER_RECEIVER;
//        }
        String sender = req.getSenderAccount() == null ? null : req.getSenderAccount().trim();
        String receiver = req.getReceiverAccount() == null ? null : req.getReceiverAccount().trim();

        if (sender != null && sender.equals(receiver)) {
            return DeclineReason.SAME_SENDER_RECEIVER;
        }

        if (req.getCurrency() == null || !ALLOWED_CURRENCIES.contains(req.getCurrency().toUpperCase())) {
            return DeclineReason.INVALID_CURRENCY;
        }

        // simple format check: ACC + digits (you can tighten later)
        if (!isValidAccount(req.getSenderAccount()) || !isValidAccount(req.getReceiverAccount())) {
            return DeclineReason.INVALID_ACCOUNT_FORMAT;
        }

        return null; // not declined by base rules
    }

    private boolean isValidAccount(String acc) {
        if (acc == null) return false;
        return acc.matches("^ACC\\d{6,12}$");
    }
}