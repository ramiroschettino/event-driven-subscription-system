package com.rappi.payment.infrastructure.adapter.in.web;

public record PaymentRequest(
        String userId,
        Double amount,
        String concept
) {}
