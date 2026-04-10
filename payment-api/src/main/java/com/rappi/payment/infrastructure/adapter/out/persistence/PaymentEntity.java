package com.rappi.payment.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class PaymentEntity {

    @Id
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String concept;

    @Column(name = "processed_at", nullable = false)
    private LocalDateTime processedAt;

    public PaymentEntity() {
    }

    public PaymentEntity(String id, String userId, Double amount, String concept, LocalDateTime processedAt) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.concept = concept;
        this.processedAt = processedAt;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public Double getAmount() { return amount; }
    public String getConcept() { return concept; }
    public LocalDateTime getProcessedAt() { return processedAt; }
}
