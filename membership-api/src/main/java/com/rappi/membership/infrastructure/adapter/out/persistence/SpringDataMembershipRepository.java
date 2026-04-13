package com.rappi.membership.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataMembershipRepository extends JpaRepository<MembershipEntity, String> {
    Optional<MembershipEntity> findByUserId(String userId);
}
