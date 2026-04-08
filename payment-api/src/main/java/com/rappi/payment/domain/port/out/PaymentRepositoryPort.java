package com.rappi.payment.domain.port.out;

import com.rappi.payment.domain.model.Payment;
import java.util.Optional;

public interface PaymentRepositoryPort {
    Payment save(Payment payment);
    Optional<Payment> findById(String id);
}
