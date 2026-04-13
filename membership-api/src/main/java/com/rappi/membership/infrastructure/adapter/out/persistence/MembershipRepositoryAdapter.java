package com.rappi.membership.infrastructure.adapter.out.persistence;

import com.rappi.membership.domain.model.Membership;
import com.rappi.membership.domain.port.out.MembershipRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MembershipRepositoryAdapter implements MembershipRepositoryPort {

    private final SpringDataMembershipRepository repository;

    public MembershipRepositoryAdapter(SpringDataMembershipRepository repository) {
        this.repository = repository;
    }

    @Override
    public Membership save(Membership membership) {
        MembershipEntity entity = new MembershipEntity(
                membership.id(),
                membership.userId(),
                membership.type(),
                membership.status(),
                membership.startDate(),
                membership.endDate()
        );
        MembershipEntity savedEntity = repository.save(entity);
        return mapToDomain(savedEntity);
    }

    @Override
    public Optional<Membership> findByUserId(String userId) {
        return repository.findByUserId(userId).map(this::mapToDomain);
    }

    private Membership mapToDomain(MembershipEntity entity) {
        return new Membership(
                entity.getId(),
                entity.getUserId(),
                entity.getType(),
                entity.getStatus(),
                entity.getStartDate(),
                entity.getEndDate()
        );
    }
}
