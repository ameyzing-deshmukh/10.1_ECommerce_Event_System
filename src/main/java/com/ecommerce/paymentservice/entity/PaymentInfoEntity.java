package com.ecommerce.paymentservice.entity;

import com.ecommerce.paymentservice.model.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class PaymentInfoEntity {

    @Id
    @GeneratedValue
    private Long paymentInfoId;
    private Long orderId;
    private String userId;
    private BigDecimal paymentAmount;
    private String paymentId;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentTs;
    @CreationTimestamp
    private LocalDateTime createTs;

    public PaymentInfoEntity() {
    }

    public PaymentInfoEntity(Long orderId, String userId, BigDecimal paymentAmount, String paymentId, LocalDateTime paymentTs) {
        this.orderId = orderId;
        this.userId = userId;
        this.paymentAmount = paymentAmount;
        this.paymentId = paymentId;
        this.paymentTs = paymentTs;
    }
}
