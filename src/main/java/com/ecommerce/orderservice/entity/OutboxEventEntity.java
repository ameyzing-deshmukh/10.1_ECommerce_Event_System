package com.ecommerce.orderservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name="outbox_event")
public class OutboxEventEntity {
    /*
    * This entity will save order events which will be picked by a scheduled job and sent to Kafka Topic*/

    @Id
    @GeneratedValue
    private UUID outboxId;
    private Long aggregateId;

    private String eventType;

    @Lob
    private String payload;
    private boolean isProcessed;
    private LocalDateTime createdAt;

}
