package com.rappi.membership.application.usecase;

import com.rappi.membership.domain.model.Membership;
import com.rappi.membership.domain.model.MembershipActivatedEvent;
import com.rappi.membership.domain.model.MembershipStatus;
import com.rappi.membership.domain.model.MembershipType;
import com.rappi.membership.domain.port.in.ActivateMembershipUseCase;
import com.rappi.membership.domain.port.out.EventPublisherPort;
import com.rappi.membership.domain.port.out.MembershipRepositoryPort;

import java.time.LocalDateTime;
import java.util.UUID;

public class ActivateMembershipUseCaseImpl implements ActivateMembershipUseCase {

    private final MembershipRepositoryPort membershipRepositoryPort;
    private final EventPublisherPort eventPublisherPort;

    public ActivateMembershipUseCaseImpl(MembershipRepositoryPort membershipRepositoryPort,
                                         EventPublisherPort eventPublisherPort) {
        this.membershipRepositoryPort = membershipRepositoryPort;
        this.eventPublisherPort = eventPublisherPort;
    }

    @Override
    public Membership activateMembership(String userId, MembershipType membershipType) {
        Membership currentMembership = membershipRepositoryPort.findByUserId(userId)
                .orElse(new Membership(UUID.randomUUID().toString(), userId, membershipType, MembershipStatus.PENDING, null, null));
        
        Membership activatedMembership = currentMembership.activate(LocalDateTime.now());
        Membership savedMembership = membershipRepositoryPort.save(activatedMembership);

        MembershipActivatedEvent event = new MembershipActivatedEvent(
                savedMembership.id(),
                savedMembership.userId(),
                savedMembership.type(),
                LocalDateTime.now()
        );
        eventPublisherPort.publishMembershipActivatedEvent(event);

        return savedMembership;
    }
}
