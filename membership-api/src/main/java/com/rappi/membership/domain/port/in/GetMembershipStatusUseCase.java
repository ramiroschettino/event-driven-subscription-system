package com.rappi.membership.domain.port.in;

import com.rappi.membership.domain.model.Membership;
import java.util.Optional;

public interface GetMembershipStatusUseCase {
    Optional<Membership> getMembershipStatus(String userId);
}
