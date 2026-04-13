package com.rappi.membership.infrastructure.adapter.in.web;

import com.rappi.membership.domain.port.in.GetMembershipStatusUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/memberships")
public class MembershipController {

    private final GetMembershipStatusUseCase getMembershipStatusUseCase;

    public MembershipController(GetMembershipStatusUseCase getMembershipStatusUseCase) {
        this.getMembershipStatusUseCase = getMembershipStatusUseCase;
    }

    @GetMapping("/status/{userId}")
    public ResponseEntity<MembershipResponse> getStatus(@PathVariable String userId) {
        return getMembershipStatusUseCase.getMembershipStatus(userId)
                .map(membership -> ResponseEntity.ok(MembershipResponse.fromDomain(membership)))
                .orElse(ResponseEntity.notFound().build());
    }
}
