package com.rappi.membership.application.usecase;

import com.rappi.membership.domain.model.Membership;
import com.rappi.membership.domain.port.in.GetMembershipStatusUseCase;
import com.rappi.membership.domain.port.out.MembershipCachePort;
import com.rappi.membership.domain.port.out.MembershipRepositoryPort;

import java.util.Optional;

public class GetMembershipStatusUseCaseImpl implements GetMembershipStatusUseCase {

    private final MembershipCachePort membershipCachePort;
    private final MembershipRepositoryPort membershipRepositoryPort;

    public GetMembershipStatusUseCaseImpl(MembershipCachePort membershipCachePort,
                                          MembershipRepositoryPort membershipRepositoryPort) {
        this.membershipCachePort = membershipCachePort;
        this.membershipRepositoryPort = membershipRepositoryPort;
    }

    @Override
    public Optional<Membership> getMembershipStatus(String userId) {
        // First try the cache
        Optional<Membership> cachedMembership = membershipCachePort.get(userId);
        if (cachedMembership.isPresent()) {
            return cachedMembership;
        }

        // If not in cache, fetch from database
        Optional<Membership> dbMembership = membershipRepositoryPort.findByUserId(userId);
        
        // Save to cache for subsequent requests
        dbMembership.ifPresent(membershipCachePort::save);
        
        return dbMembership;
    }
}
