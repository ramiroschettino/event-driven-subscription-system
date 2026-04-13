package com.rappi.membership.infrastructure.adapter.in.web;

public record CreateUserRequest(
        String email,
        String name
) {}
