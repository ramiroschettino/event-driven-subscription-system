package com.rappi.payment.domain.port.out;

public interface UserValidationPort {
    boolean userExists(String userId);
}
