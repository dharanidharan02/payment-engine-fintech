package com.fintech.payment_engine.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Payment request payload")
public class PaymentRequest {

    @NotNull
    @DecimalMin(value = "0.01")
    @Schema(example = "200.00", description = "Transaction amount")
    private BigDecimal amount;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be 3 uppercase letters (e.g., CAD)")
    @Schema(example = "USD", description = "Currency code")
    private String currency;

    @NotBlank
    @Size(min = 4, max = 20)
    @Schema(example = "ACC123456", description = "Sender account")
    private String senderAccount;

    @NotBlank
    @Size(min = 4, max = 20)
    @Schema(example = "ACC654321", description = "Receiver account")
    private String receiverAccount;

    // getters & setters
    public String getSenderAccount() { return senderAccount; }
    public void setSenderAccount(String senderAccount) { this.senderAccount = senderAccount; }

    public String getReceiverAccount() { return receiverAccount; }
    public void setReceiverAccount(String receiverAccount) { this.receiverAccount = receiverAccount; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}