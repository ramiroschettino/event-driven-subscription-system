package com.rappi.payment.domain.port.out;

public interface PaymentGatewayPort {
    boolean charge(String userId, Double amount);
}
