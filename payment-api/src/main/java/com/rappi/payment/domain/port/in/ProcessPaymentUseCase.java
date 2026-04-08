package com.rappi.payment.domain.port.in;

import com.rappi.payment.domain.model.Payment;

public interface ProcessPaymentUseCase {
    Payment processPayment(String userId, Double amount, String concept);
}
