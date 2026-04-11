package com.rappi.membership.domain.port.in;

import com.rappi.membership.domain.model.User;

public interface CreateUserUseCase {
    User createUser(String email, String name);
}
