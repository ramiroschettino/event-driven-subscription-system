package com.rappi.membership.domain.service;

import com.rappi.membership.domain.model.Membership;
import java.time.LocalDateTime;

public class MembershipValidationService {
    
    public boolean isValid(Membership membership, LocalDateTime currentDate) {
        if (membership == null) {
            return false;
        }
        return membership.isActive(currentDate);
    }
}
