package com.rappi.membership.infrastructure.config;

import com.rappi.membership.application.usecase.ActivateMembershipUseCaseImpl;
import com.rappi.membership.application.usecase.GetMembershipStatusUseCaseImpl;
import com.rappi.membership.application.usecase.CreateUserUseCaseImpl;
import com.rappi.membership.application.usecase.GetUserUseCaseImpl;
import com.rappi.membership.domain.port.in.ActivateMembershipUseCase;
import com.rappi.membership.domain.port.in.CreateUserUseCase;
import com.rappi.membership.domain.port.in.GetUserUseCase;
import com.rappi.membership.domain.port.in.GetMembershipStatusUseCase;
import com.rappi.membership.domain.port.out.EventPublisherPort;
import com.rappi.membership.domain.port.out.UserRepositoryPort;
import com.rappi.membership.domain.port.out.MembershipCachePort;
import com.rappi.membership.domain.port.out.MembershipRepositoryPort;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public ActivateMembershipUseCase activateMembershipUseCase(
            MembershipRepositoryPort membershipRepositoryPort,
            EventPublisherPort eventPublisherPort) {
        return new ActivateMembershipUseCaseImpl(
                membershipRepositoryPort,
                eventPublisherPort
        );
    }

    @Bean
    public GetMembershipStatusUseCase getMembershipStatusUseCase(
            MembershipCachePort membershipCachePort,
            MembershipRepositoryPort membershipRepositoryPort) {
        return new GetMembershipStatusUseCaseImpl(
                membershipCachePort,
                membershipRepositoryPort
        );
    }

    @Bean
    public CreateUserUseCase createUserUseCase(UserRepositoryPort userRepositoryPort) {
        return new CreateUserUseCaseImpl(userRepositoryPort);
    }
    
    @Bean
    public GetUserUseCase getUserUseCase(UserRepositoryPort userRepositoryPort) {
        return new GetUserUseCaseImpl(userRepositoryPort);
    }
}
