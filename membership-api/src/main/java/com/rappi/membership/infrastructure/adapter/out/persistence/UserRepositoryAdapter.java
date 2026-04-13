package com.rappi.membership.infrastructure.adapter.out.persistence;

import com.rappi.membership.domain.model.User;
import com.rappi.membership.domain.port.out.UserRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final SpringDataUserRepository repository;

    public UserRepositoryAdapter(SpringDataUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = new UserEntity(user.id(), user.email(), user.name());
        repository.save(entity);
        return user;
    }

    @Override
    public Optional<User> findById(String id) {
        return repository.findById(id).map(entity -> new User(entity.getId(), entity.getEmail(), entity.getName()));
    }
}
