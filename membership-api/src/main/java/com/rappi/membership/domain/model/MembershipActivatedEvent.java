package com.rappi.membership.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record MembershipActivatedEvent(
    String eventId,
    String membershipId,
    String userId,
    MembershipType membershipType,
    LocalDateTime occurredAt
) {
    public MembershipActivatedEvent(String membershipId, String userId, MembershipType membershipType, LocalDateTime occurredAt) {
        this(UUID.randomUUID().toString(), membershipId, userId, membershipType, occurredAt);
    }
}
