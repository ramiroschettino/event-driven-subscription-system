package com.rappi.membership.infrastructure.adapter.out.messaging;

import com.rappi.membership.infrastructure.adapter.out.persistence.OutboxEventEntity;
import com.rappi.membership.infrastructure.adapter.out.persistence.SpringDataOutboxEventRepository;
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
                // Publish to Kafka topic
                kafkaTemplate.send("membership.events", event.getId(), event.getPayload()).get(); // Wait for ack to guarantee delivery to broker
                
                // Mark as processed
                event.setProcessed(true);
                outboxRepository.save(event);
                log.info("Successfully published and marked outbox event as processed: {}", event.getId());
            } catch (Exception e) {
                log.error("Failed to publish outbox event: {}", event.getId(), e);
                // We don't mark as processed, so it will be retried next time
            }
        }
    }
}
