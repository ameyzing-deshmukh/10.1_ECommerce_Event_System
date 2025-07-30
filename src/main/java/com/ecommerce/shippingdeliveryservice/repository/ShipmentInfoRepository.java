package com.ecommerce.shippingdeliveryservice.repository;

import com.ecommerce.shippingdeliveryservice.entity.ShipmentInfoEntity;
import com.ecommerce.shippingdeliveryservice.model.ShippingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipmentInfoRepository extends JpaRepository<ShipmentInfoEntity, Long> {
    public List<ShipmentInfoEntity> findAllByStatus(ShippingStatus status);
}
