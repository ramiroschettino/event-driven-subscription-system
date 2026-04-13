package com.rappi.membership.infrastructure.adapter.in.web;

import com.rappi.membership.domain.model.User;
import com.rappi.membership.domain.port.in.CreateUserUseCase;
import com.rappi.membership.domain.port.in.GetUserUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetUserUseCase getUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase, GetUserUseCase getUserUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.getUserUseCase = getUserUseCase;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        User user = createUserUseCase.createUser(request.email(), request.name());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromDomain(user));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String userId) {
        return getUserUseCase.getUserById(userId)
                .map(user -> ResponseEntity.ok(UserResponse.fromDomain(user)))
                .orElse(ResponseEntity.notFound().build());
    }
}
