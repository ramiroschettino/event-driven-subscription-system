package com.rappi.membership.infrastructure.adapter.in.web;

import com.rappi.membership.domain.model.User;

public record UserResponse(
        String id,
        String email,
        String name
) {
    public static UserResponse fromDomain(User user) {
        return new UserResponse(user.id(), user.email(), user.name());
    }
}
