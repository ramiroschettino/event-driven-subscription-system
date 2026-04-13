package com.rappi.membership.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataOutboxEventRepository extends JpaRepository<OutboxEventEntity, String> {
    List<OutboxEventEntity> findByProcessedFalseOrderByCreatedAtAsc();
}
