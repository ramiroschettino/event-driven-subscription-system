package com.rappi.membership.infrastructure.adapter.out.persistence;

import com.rappi.membership.domain.model.MembershipStatus;
import com.rappi.membership.domain.model.MembershipType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "memberships")
public class MembershipEntity {

    @Id
    private String id;
    
    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipType type;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipStatus status;
    
    @Column(name = "start_date")
    private LocalDateTime startDate;
    
    @Column(name = "end_date")
    private LocalDateTime endDate;

    public MembershipEntity() {
    }

    public MembershipEntity(String id, String userId, MembershipType type, MembershipStatus status, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public MembershipType getType() { return type; }
    public void setType(MembershipType type) { this.type = type; }
    public MembershipStatus getStatus() { return status; }
    public void setStatus(MembershipStatus status) { this.status = status; }
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
}
