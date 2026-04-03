package com.fintech.payment_engine.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fintech.payment_engine.dto.PaymentDetailsResponse;
import com.fintech.payment_engine.dto.PaymentRequest;
import com.fintech.payment_engine.dto.PaymentResponse;
import com.fintech.payment_engine.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;




@RestController
@RequestMapping("/api/payments")
public class PaymentController {
	
	
	

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    @Operation(summary = "Create a payment transaction",
            description = "Processes a payment with validation, fraud checks, and idempotency handling")

    @ApiResponses(value = {
    		@ApiResponse(responseCode = "201", description = "Transaction created successfully"),
    		@ApiResponse(responseCode = "409", description = "Idempotency conflict"),
    		@ApiResponse(responseCode = "400", description = "Validation error")
    })
    
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
    
	/*
	 * @PostMapping
	 * 
	 * @ResponseStatus(HttpStatus.CREATED) public PaymentResponse create(
	 * 
	 * @RequestHeader(value = "Idempotency-Key", required = false) String
	 * idempotencyKey,
	 * 
	 * @Valid @RequestBody PaymentRequest request) {
	 * 
	 * return paymentService.createPayment(request, idempotencyKey); }
	 */
   