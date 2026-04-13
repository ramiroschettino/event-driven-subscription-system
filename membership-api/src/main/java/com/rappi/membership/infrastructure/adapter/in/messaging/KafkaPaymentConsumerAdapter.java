package com.rappi.membership.infrastructure.adapter.in.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rappi.membership.domain.model.MembershipType;
import com.rappi.membership.domain.model.PaymentApprovedEvent;
import com.rappi.membership.domain.port.in.ActivateMembershipUseCase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class KafkaPaymentConsumerAdapter {

    private static final Logger log = LoggerFactory.getLogger(KafkaPaymentConsumerAdapter.class);

    private final ActivateMembershipUseCase activateMembershipUseCase;
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate redisTemplate;

    public KafkaPaymentConsumerAdapter(ActivateMembershipUseCase activateMembershipUseCase, ObjectMapper objectMapper, StringRedisTemplate redisTemplate) {
        this.activateMembershipUseCase = activateMembershipUseCase;
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
    }

    @KafkaListener(topics = "payment.events", groupId = "membership-payment-group")
    public void consumePaymentEvent(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        String eventId = record.key();
        String payload = record.value();

        log.info("Received PaymentApprovedEvent via Kafka: {}", eventId);

        String idempotencyKey = "processed_payment_event:" + eventId;
        Boolean alreadyProcessed = redisTemplate.opsForValue().setIfAbsent(idempotencyKey, "true", Duration.ofDays(1));

        if (Boolean.FALSE.equals(alreadyProcessed)) {
            log.info("Payment Event {} was already processed. Skipping.", eventId);
            acknowledgment.acknowledge();
            return;
        }

        try {
            PaymentApprovedEvent event = objectMapper.readValue(payload, PaymentApprovedEvent.class);
            
            // Extract MembershipType from the concept or map it. Simplification for demo:
            MembershipType type = MembershipType.PRO;
            if (event.concept().contains("PLUS")) {
                type = MembershipType.PRO_PLUS;
            }

            log.info("Activating membership for user: {}", event.userId());
            activateMembershipUseCase.activateMembership(event.userId(), type);

            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Error processing Payment Event {}, routing to DLQ logic", eventId, e);
            redisTemplate.delete(idempotencyKey);
            throw new RuntimeException("Failed to process payment event " + eventId, e);
        }
    }
}
