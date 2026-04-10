package com.rappi.payment.infrastructure.adapter.in.web;

import com.rappi.payment.domain.model.Payment;
import java.time.LocalDateTime;

public record PaymentResponse(
        String id,
        String userId,
        Double amount,
        String concept,
        LocalDateTime processedAt
) {
    public static PaymentResponse fromDomain(Payment payment) {
        return new PaymentResponse(payment.id(), payment.userId(), payment.amount(), payment.concept(), payment.processedAt());
    }
}
