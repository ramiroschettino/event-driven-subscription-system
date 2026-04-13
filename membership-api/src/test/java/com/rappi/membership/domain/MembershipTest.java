package com.rappi.membership.domain;

import com.rappi.membership.domain.model.Membership;
import com.rappi.membership.domain.model.MembershipStatus;
import com.rappi.membership.domain.model.MembershipType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MembershipTest {

    @Test
    void testActivateMembership() {
        // Arrange
        Membership pendingMembership = new Membership(
                "123", "user1", MembershipType.PRO, MembershipStatus.PENDING, null, null
        );
        LocalDateTime now = LocalDateTime.now();

        // Act
        Membership activated = pendingMembership.activate(now);

        // Assert
        assertEquals(MembershipStatus.ACTIVE, activated.status());
        assertEquals(now, activated.startDate());
        assertEquals(now.plusMonths(1), activated.endDate());
        assertEquals(MembershipType.PRO, activated.type());
    }

    @Test
    void testIsActiveReturnsTrueWhenActiveAndNotExpired() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Membership activeMembership = new Membership(
                "123", "user1", MembershipType.PRO, MembershipStatus.ACTIVE, now, now.plusDays(10)
        );

        // Act
        boolean isActive = activeMembership.isActive(now.plusDays(5));

        // Assert
        assertTrue(isActive);
    }

    @Test
    void testIsActiveReturnsFalseWhenExpired() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Membership activeMembership = new Membership(
                "123", "user1", MembershipType.PRO, MembershipStatus.ACTIVE, now, now.plusDays(10)
        );

        // Act
        boolean isActive = activeMembership.isActive(now.plusDays(11));

        // Assert
        assertFalse(isActive);
    }
}
