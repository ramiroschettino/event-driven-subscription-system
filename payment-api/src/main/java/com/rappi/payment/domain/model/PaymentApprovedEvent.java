package com.rappi.payment.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentApprovedEvent(
    String eventId,
    String paymentId,
    String userId,
    String concept,
    LocalDateTime occurredAt
) {
    public PaymentApprovedEvent(String paymentId, String userId, String concept, LocalDateTime occurredAt) {
        this(UUID.randomUUID().toString(), paymentId, userId, concept, occurredAt);
    }
}
