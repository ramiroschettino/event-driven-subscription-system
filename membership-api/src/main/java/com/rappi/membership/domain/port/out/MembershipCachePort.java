package com.rappi.membership.domain.port.out;

import com.rappi.membership.domain.model.Membership;
import java.util.Optional;

public interface MembershipCachePort {
    void save(Membership membership);
    Optional<Membership> get(String userId);
}
