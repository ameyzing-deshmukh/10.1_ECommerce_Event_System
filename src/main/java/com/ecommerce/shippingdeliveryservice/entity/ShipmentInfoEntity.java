package com.ecommerce.shippingdeliveryservice.entity;

import com.ecommerce.shippingdeliveryservice.model.ShippingStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class ShipmentInfoEntity {

    @Id
    @GeneratedValue
    private Long shipmentInfoId;
    private String shippingId;
    private String receiverName;
    private String shippingPartnerName;
    private String shippingAddress;
    private LocalDateTime shippingDate;
    @Enumerated(EnumType.STRING)
    private ShippingStatus status;
    @CreationTimestamp
    private LocalDateTime createTs;
    private LocalDate estimatedDateOfDelivery;

    public ShipmentInfoEntity() {
    }

    public ShipmentInfoEntity(String shippingId, String receiverName, String shippingPartnerName, String shippingAddress, LocalDateTime shippingDate, ShippingStatus status, LocalDate estimatedDateOfDelivery) {
        this.shippingId = shippingId;
        this.receiverName = receiverName;
        this.shippingPartnerName = shippingPartnerName;
        this.shippingAddress = shippingAddress;
        this.shippingDate = shippingDate;
        this.status = status;
        this.estimatedDateOfDelivery = estimatedDateOfDelivery;
    }
}
