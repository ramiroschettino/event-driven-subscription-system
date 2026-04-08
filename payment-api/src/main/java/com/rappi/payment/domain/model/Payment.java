package com.rappi.payment.domain.model;

import java.time.LocalDateTime;

public record Payment(
    String id,
    String userId,
    Double amount,
    String concept,
    LocalDateTime processedAt
) {
}
