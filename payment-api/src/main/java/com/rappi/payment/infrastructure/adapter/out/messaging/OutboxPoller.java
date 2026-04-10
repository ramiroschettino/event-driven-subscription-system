package com.rappi.payment.infrastructure.adapter.out.messaging;

import com.rappi.payment.infrastructure.adapter.out.persistence.OutboxEventEntity;
import com.rappi.payment.infrastructure.adapter.out.persistence.SpringDataOutboxEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OutboxPoller {

    private static final Logger log = LoggerFactory.getLogger(OutboxPoller.class);
    private final SpringDataOutboxEventRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxPoller(SpringDataOutboxEventRepository outboxRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 5000)
    public void pollOutbox() {
        List<OutboxEventEntity> events = outboxRepository.findByProcessedFalseOrderByCreatedAtAsc();
        for (OutboxEventEntity event : events) {
            try {
                // Publish to payment.events
                kafkaTemplate.send("payment.events", event.getId(), event.getPayload()).get();
                event.setProcessed(true);
                outboxRepository.save(event);
                log.info("Published payment outbox event {}", event.getId());
            } catch (Exception e) {
                log.error("Failed to publish payment event", e);
            }
        }
    }
}
