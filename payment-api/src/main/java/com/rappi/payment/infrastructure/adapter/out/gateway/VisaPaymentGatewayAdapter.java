package com.rappi.payment.infrastructure.adapter.out.gateway;

import com.rappi.payment.domain.port.out.PaymentGatewayPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class VisaPaymentGatewayAdapter implements PaymentGatewayPort {

    private static final Logger log = LoggerFactory.getLogger(VisaPaymentGatewayAdapter.class);
    private final Random random = new Random();

    @Override
    @CircuitBreaker(name = "visaService", fallbackMethod = "chargeFallback")
    @Retry(name = "visaService")
    public boolean charge(String userId, Double amount) {
        if (random.nextDouble() < 0.2) {
            log.warn("VISA Gateway simulation failed for user {}", userId);
            throw new RuntimeException("VISA API Connection Timeout");
        }

        log.info("VISA transaction successful.");
        return true;
    }

    public boolean chargeFallback(String userId, Double amount, Throwable t) {
        log.warn("Payment fallback returned false due to: {}", t.getMessage());
        return false;
    }
}
