package com.rappi.payment.infrastructure.config;

import com.rappi.payment.application.usecase.ProcessPaymentUseCaseImpl;
import com.rappi.payment.domain.port.in.ProcessPaymentUseCase;
import com.rappi.payment.domain.port.out.EventPublisherPort;
import com.rappi.payment.domain.port.out.PaymentGatewayPort;
import com.rappi.payment.domain.port.out.PaymentRepositoryPort;
import com.rappi.payment.domain.port.out.UserValidationPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public ProcessPaymentUseCase processPaymentUseCase(
            PaymentGatewayPort paymentGatewayPort,
            PaymentRepositoryPort paymentRepositoryPort,
            EventPublisherPort eventPublisherPort,
            UserValidationPort userValidationPort) {
        return new ProcessPaymentUseCaseImpl(
                paymentGatewayPort,
                paymentRepositoryPort,
                eventPublisherPort,
                userValidationPort
        );
    }
}
