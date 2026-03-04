package com.fintech.payment_engine.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.fintech.payment_engine.dto.PaymentDetailsResponse;
import com.fintech.payment_engine.dto.PaymentRequest;
import com.fintech.payment_engine.dto.PaymentResponse;
import com.fintech.payment_engine.service.PaymentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse create(
            @Valid @RequestBody PaymentRequest request,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey
    ) {
        return paymentService.createPayment(request, idempotencyKey);
    }

    @GetMapping("/{id}")
    public PaymentDetailsResponse get(@PathVariable UUID id) {
        return paymentService.getTransaction(id);
    }

    @GetMapping
    public List<PaymentDetailsResponse> list(@RequestParam(defaultValue = "20") int limit) {
        return paymentService.listTransactions(limit);
    }
}