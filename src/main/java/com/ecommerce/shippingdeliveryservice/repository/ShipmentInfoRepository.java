package com.ecommerce.shippingdeliveryservice.repository;

import com.ecommerce.shippingdeliveryservice.entity.ShipmentInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentInfoRepository extends JpaRepository<ShipmentInfoEntity, Long> {
}
