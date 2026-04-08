package com.fintech.payment_engine.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fintech.payment_engine.dto.PaymentDetailsResponse;
import com.fintech.payment_engine.dto.PaymentRequest;
import com.fintech.payment_engine.dto.PaymentResponse;
import com.fintech.payment_engine.exception.IdempotencyConflictException;
import com.fintech.payment_engine.exception.NotFoundException;
import com.fintech.payment_engine.model.DeclineReason;
import com.fintech.payment_engine.model.IdempotencyRecord;
import com.fintech.payment_engine.model.Transaction;
import com.fintech.payment_engine.model.TransactionDecision;
import com.fintech.payment_engine.repository.IdempotencyRepository;
import com.fintech.payment_engine.repository.TransactionRepository;

@Service
public class PaymentService {

    private final TransactionRepository transactionRepository;
    private final IdempotencyRepository idempotencyRepository;
    private final FraudService fraudService;
    private final PaymentRules paymentRules;
    private final KafkaProducerService kafkaProducerService;

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    
    public PaymentService(TransactionRepository transactionRepository,
                          IdempotencyRepository idempotencyRepository,
                          FraudService fraudService,
                          PaymentRules paymentRules,
                          KafkaProducerService kafkaProducerService) {
        this.transactionRepository = transactionRepository;
        this.idempotencyRepository = idempotencyRepository;
        this.fraudService = fraudService;
        this.paymentRules = paymentRules;
        this.kafkaProducerService = kafkaProducerService;

    }
    
   

    
    @Transactional
    public PaymentResponse createPayment(PaymentRequest req, String idempotencyKey) {

        // 1) If idempotency key provided -> return existing response (or reject if payload differs)
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            String requestHash = hashRequest(req);

            var existing = idempotencyRepository.findByIdempotencyKey(idempotencyKey);
            if (existing.isPresent()) {
                IdempotencyRecord record = existing.get();
                if (!record.getRequestHash().equals(requestHash)) {
//                	public class IdempotencyConflictException extends RuntimeException {
//                	    public IdempotencyConflictException(String message) {
//                	        super(message);
//                	    }
//                	}
                    // Same key used with different payload = client bug (FinTech systems must reject)
                	throw new IdempotencyConflictException("Idempotency-Key reuse with different request body is not allowed.");
                }
                Transaction oldTx = transactionRepository.findById(record.getTransactionId())
                        .orElseThrow(() -> new NotFoundException("Transaction not found for idempotency key."));
                return toCreateResponse(oldTx);
            }

            // else continue normal creation, then store record
            Transaction saved = createAndSave(req);
            IdempotencyRecord record = new IdempotencyRecord();
            record.setIdempotencyKey(idempotencyKey);
            record.setRequestHash(requestHash);
            record.setTransactionId(saved.getId());
            idempotencyRepository.save(record);

            return toCreateResponse(saved);
        }

        // 2) No idempotency -> normal create
        Transaction saved = createAndSave(req);
        return toCreateResponse(saved);
    }
    

    public PaymentDetailsResponse getTransaction(UUID id) {
        Transaction tx = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found: " + id));
        return toDetailsResponse(tx);
    }

    public List<PaymentDetailsResponse> listTransactions(int limit) {
        int safeLimit = Math.min(Math.max(limit, 1), 100);
        // If you don’t have paging in repo yet, we’ll do simple fetch-all then limit.
        // Better: add Pageable later. For now:
        return transactionRepository.findTop100ByOrderByCreatedAtDesc()
                .stream()
                .limit(safeLimit)
                .map(this::toDetailsResponse)
                .toList();
    }

    private Transaction createAndSave(PaymentRequest req) {
        Transaction tx = new Transaction();
        tx.setSenderAccount(req.getSenderAccount());
        tx.setReceiverAccount(req.getReceiverAccount());
        tx.setAmount(req.getAmount());
        tx.setCurrency(req.getCurrency());

        DeclineReason declineReason = paymentRules.evaluateDeclineReason(req);
        if (declineReason != null) {
            tx.setDecision(TransactionDecision.DECLINED);
            tx.setDeclineReason(declineReason);
        } else if (fraudService.isSuspicious(req)) {
            tx.setDecision(TransactionDecision.FLAGGED);
        } else {
            tx.setDecision(TransactionDecision.APPROVED);
        }

        Transaction saved = transactionRepository.save(tx);

        String event = "Transaction " + saved.getId() + " status: " + saved.getDecision();
        kafkaProducerService.sendEvent(event);

        return saved;
    }
    
    private PaymentResponse toCreateResponse(Transaction tx) {
        String message;
        if (tx.getDecision() == TransactionDecision.DECLINED) {
            message = "Transaction declined: " + tx.getDeclineReason();
        } else if (tx.getDecision() == TransactionDecision.FLAGGED) {
            message = "\"Transaction flagged for review: HIGH_AMOUNT\"";
        } else {
            message = "Transaction approved";
        }

        // keep your existing PaymentResponse shape (minimal change)
        return new PaymentResponse(
                tx.getId(),
                tx.getDecision(),          // update PaymentResponse type if needed
                tx.getAmount(),
                tx.getCurrency(),
                tx.getCreatedAt(),
                tx.getDeclineReason(),
                message
        );
    }

    private PaymentDetailsResponse toDetailsResponse(Transaction tx) {
        return new PaymentDetailsResponse(
                tx.getId(),
                tx.getDecision(),
                tx.getAmount(),
                tx.getCurrency(),
                tx.getSenderAccount(),
                tx.getReceiverAccount(),
                tx.getCreatedAt(),
                tx.getDeclineReason()
        );
    }

    private String hashRequest(PaymentRequest req) {
        // stable hash over key fields
        String payload = String.join("|",
                safe(req.getSenderAccount()),
                safe(req.getReceiverAccount()),
                safe(req.getCurrency()),
                req.getAmount() == null ? "" : req.getAmount().toPlainString()
        );

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] out = md.digest(payload.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(out);
        } catch (Exception e) {
            throw new RuntimeException("Unable to hash request", e);
        }
    }

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }
    
    
}