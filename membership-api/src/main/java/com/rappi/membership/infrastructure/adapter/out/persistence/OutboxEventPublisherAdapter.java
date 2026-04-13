package com.rappi.membership.infrastructure.adapter.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rappi.membership.domain.model.MembershipActivatedEvent;
import com.rappi.membership.domain.port.out.EventPublisherPort;
import org.springframework.stereotype.Component;

@Component
public class OutboxEventPublisherAdapter implements EventPublisherPort {

    private final SpringDataOutboxEventRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public OutboxEventPublisherAdapter(SpringDataOutboxEventRepository outboxRepository, ObjectMapper objectMapper) {
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishMembershipActivatedEvent(MembershipActivatedEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            OutboxEventEntity outboxEvent = new OutboxEventEntity(
                    event.eventId(),
                    event.getClass().getSimpleName(),
                    payload,
                    event.occurredAt()
            );
            outboxRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize domain event", e);
        }
    }
}
