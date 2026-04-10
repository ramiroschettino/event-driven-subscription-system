package com.rappi.payment.infrastructure.adapter.out.persistence;

import com.rappi.payment.domain.model.Payment;
import com.rappi.payment.domain.port.out.PaymentRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PaymentRepositoryAdapter implements PaymentRepositoryPort {

    private final SpringDataPaymentRepository repository;

    public PaymentRepositoryAdapter(SpringDataPaymentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Payment save(Payment payment) {
        repository.save(new PaymentEntity(payment.id(), payment.userId(), payment.amount(), payment.concept(), payment.processedAt()));
        return payment;
    }

    @Override
    public Optional<Payment> findById(String id) {
        return repository.findById(id).map(entity -> new Payment(entity.getId(), entity.getUserId(), entity.getAmount(), entity.getConcept(), entity.getProcessedAt()));
    }
}
