package com.rappi.membership.domain.model;

import java.time.LocalDateTime;

public record PaymentApprovedEvent(
    String eventId,
    String paymentId,
    String userId,
    String concept,
    LocalDateTime occurredAt
) {
}
