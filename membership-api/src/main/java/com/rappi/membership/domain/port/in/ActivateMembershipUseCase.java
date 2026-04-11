package com.rappi.membership.domain.port.in;

import com.rappi.membership.domain.model.Membership;
import com.rappi.membership.domain.model.MembershipType;

public interface ActivateMembershipUseCase {
    Membership activateMembership(String userId, MembershipType membershipType);
}
