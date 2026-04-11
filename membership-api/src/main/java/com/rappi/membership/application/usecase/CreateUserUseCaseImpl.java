package com.rappi.membership.application.usecase;

import com.rappi.membership.domain.model.User;
import com.rappi.membership.domain.port.in.CreateUserUseCase;
import com.rappi.membership.domain.port.out.UserRepositoryPort;

import java.util.UUID;

public class CreateUserUseCaseImpl implements CreateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public CreateUserUseCaseImpl(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public User createUser(String email, String name) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        User newUser = new User(UUID.randomUUID().toString(), email, name);
        return userRepositoryPort.save(newUser);
    }
}
