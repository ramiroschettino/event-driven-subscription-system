package com.rappi.payment.infrastructure.adapter.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rappi.payment.domain.model.PaymentApprovedEvent;
import com.rappi.payment.domain.port.out.EventPublisherPort;
import org.springframework.stereotype.Component;

@Component
public class OutboxEventPublisherAdapter implements EventPublisherPort {

    private final SpringDataOutboxEventRepository repository;
    private final ObjectMapper objectMapper;

    public OutboxEventPublisherAdapter(SpringDataOutboxEventRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(PaymentApprovedEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            repository.save(new OutboxEventEntity(event.eventId(), "PaymentApprovedEvent", payload, event.occurredAt()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error publishing outbox event", e);
        }
    }
}
