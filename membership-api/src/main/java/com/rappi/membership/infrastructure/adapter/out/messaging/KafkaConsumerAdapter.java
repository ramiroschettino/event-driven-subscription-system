package com.rappi.membership.infrastructure.adapter.out.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rappi.membership.domain.model.MembershipActivatedEvent;
import com.rappi.membership.domain.port.out.MembershipCachePort;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class KafkaConsumerAdapter {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerAdapter.class);
    
    private final MembershipCachePort cachePort;
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate redisTemplate;

    public KafkaConsumerAdapter(MembershipCachePort cachePort, ObjectMapper objectMapper, StringRedisTemplate redisTemplate) {
        this.cachePort = cachePort;
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
    }

    @KafkaListener(topics = "membership.events", groupId = "membership-group")
    public void consumeMembershipEvent(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        String eventId = record.key();
        String payload = record.value();
        
        log.info("Received event via Kafka: {}", eventId);

        // Idempotency Check using Redis (key: processed_event:{eventId})
        String idempotencyKey = "processed_event:" + eventId;
        Boolean alreadyProcessed = redisTemplate.opsForValue().setIfAbsent(idempotencyKey, "true", Duration.ofDays(1));
        
        if (Boolean.FALSE.equals(alreadyProcessed)) {
            log.info("Event {} was already processed. Skipping.", eventId);
            acknowledgment.acknowledge();
            return;
        }

        try {
            MembershipActivatedEvent event = objectMapper.readValue(payload, MembershipActivatedEvent.class);
            // Updating cache based on event (simplified: doing nothing since cache update logic is specific)
            // Or maybe invalidate cache for that user
            redisTemplate.delete("membership:" + event.userId());
            log.info("Invalidated cache for user {}", event.userId());
            
            // Acknowledge the message
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Error processing event {}, routing to DLQ logic", eventId, e);
            // Remove from idempotency since it failed
            redisTemplate.delete(idempotencyKey);
            
            // Re-throw to trigger DLQ logic configured in KafkaListenerContainerFactory
            throw new RuntimeException("Failed to process event " + eventId, e);
        }
    }
}
