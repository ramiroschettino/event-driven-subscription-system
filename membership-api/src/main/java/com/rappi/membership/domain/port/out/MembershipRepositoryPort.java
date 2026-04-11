package com.rappi.membership.domain.port.out;

import com.rappi.membership.domain.model.Membership;
import java.util.Optional;

public interface MembershipRepositoryPort {
    Membership save(Membership membership);
    Optional<Membership> findByUserId(String userId);
}
