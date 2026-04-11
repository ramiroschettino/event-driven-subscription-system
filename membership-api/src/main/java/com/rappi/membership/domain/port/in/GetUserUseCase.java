package com.rappi.membership.domain.port.in;

import com.rappi.membership.domain.model.User;
import java.util.Optional;

public interface GetUserUseCase {
    Optional<User> getUserById(String id);
}
