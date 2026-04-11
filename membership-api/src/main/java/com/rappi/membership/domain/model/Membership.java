package com.rappi.membership.domain.model;

import java.time.LocalDateTime;

public record Membership(
    String id,
    String userId,
    MembershipType type,
    MembershipStatus status,
    LocalDateTime startDate,
    LocalDateTime endDate
) {
    public Membership activate(LocalDateTime activationDate) {
        LocalDateTime expirationDate = activationDate.plusMonths(1);
        return new Membership(
            this.id,
            this.userId,
            this.type,
            MembershipStatus.ACTIVE,
            activationDate,
            expirationDate
        );
    }
    
    public boolean isActive(LocalDateTime currentDate) {
        if (this.status != MembershipStatus.ACTIVE) {
            return false;
        }
        if (this.endDate != null && currentDate.isAfter(this.endDate)) {
            return false;
        }
        return true;
    }
}
