package com.rappi.payment.domain.port.out;

import com.rappi.payment.domain.model.PaymentApprovedEvent;

public interface EventPublisherPort {
    void publish(PaymentApprovedEvent event);
}
