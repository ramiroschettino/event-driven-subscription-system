package com.rappi.membership.application.usecase;

import com.rappi.membership.domain.model.User;
import com.rappi.membership.domain.port.in.GetUserUseCase;
import com.rappi.membership.domain.port.out.UserRepositoryPort;

import java.util.Optional;

public class GetUserUseCaseImpl implements GetUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public GetUserUseCaseImpl(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepositoryPort.findById(id);
    }
}
