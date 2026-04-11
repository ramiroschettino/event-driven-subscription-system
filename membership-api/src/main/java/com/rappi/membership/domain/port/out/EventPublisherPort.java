package com.rappi.membership.domain.port.out;

import com.rappi.membership.domain.model.MembershipActivatedEvent;

public interface EventPublisherPort {
    void publishMembershipActivatedEvent(MembershipActivatedEvent event);
}
