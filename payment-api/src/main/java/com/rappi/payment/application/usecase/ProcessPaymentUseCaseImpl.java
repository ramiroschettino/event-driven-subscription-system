package com.rappi.payment.application.usecase;

import com.rappi.payment.domain.model.Payment;
import com.rappi.payment.domain.model.PaymentApprovedEvent;
import com.rappi.payment.domain.port.in.ProcessPaymentUseCase;
import com.rappi.payment.domain.port.out.EventPublisherPort;
import com.rappi.payment.domain.port.out.PaymentGatewayPort;
import com.rappi.payment.domain.port.out.PaymentRepositoryPort;
import com.rappi.payment.domain.port.out.UserValidationPort;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProcessPaymentUseCaseImpl implements ProcessPaymentUseCase {

    private final PaymentGatewayPort paymentGatewayPort;
    private final PaymentRepositoryPort paymentRepositoryPort;
    private final EventPublisherPort eventPublisherPort;
    private final UserValidationPort userValidationPort;

    public ProcessPaymentUseCaseImpl(PaymentGatewayPort paymentGatewayPort,
                                     PaymentRepositoryPort paymentRepositoryPort,
                                     EventPublisherPort eventPublisherPort,
                                     UserValidationPort userValidationPort) {
        this.paymentGatewayPort = paymentGatewayPort;
        this.paymentRepositoryPort = paymentRepositoryPort;
        this.eventPublisherPort = eventPublisherPort;
        this.userValidationPort = userValidationPort;
    }

    @Override
    public Payment processPayment(String userId, Double amount, String concept) {
        if (!userValidationPort.userExists(userId)) {
            throw new IllegalArgumentException("User with ID " + userId + " does not exist.");
        }

        boolean charged = paymentGatewayPort.charge(userId, amount);
        if (!charged) {
            throw new IllegalStateException("External Payment Gateway declined or failed.");
        }

        Payment payment = new Payment(UUID.randomUUID().toString(), userId, amount, concept, LocalDateTime.now());
        Payment saved = paymentRepositoryPort.save(payment);

        PaymentApprovedEvent event = new PaymentApprovedEvent(saved.id(), saved.userId(), saved.concept(), saved.processedAt());
        eventPublisherPort.publish(event);

        return saved;
    }
}
