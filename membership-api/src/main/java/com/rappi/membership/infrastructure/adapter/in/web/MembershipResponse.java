package com.rappi.membership.infrastructure.adapter.in.web;

import com.rappi.membership.domain.model.Membership;
import com.rappi.membership.domain.model.MembershipStatus;
import com.rappi.membership.domain.model.MembershipType;

import java.time.LocalDateTime;

public record MembershipResponse(
        String id,
        String userId,
        MembershipType type,
        MembershipStatus status,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
    public static MembershipResponse fromDomain(Membership membership) {
        return new MembershipResponse(
                membership.id(),
                membership.userId(),
                membership.type(),
                membership.status(),
                membership.startDate(),
                membership.endDate()
        );
    }
}
