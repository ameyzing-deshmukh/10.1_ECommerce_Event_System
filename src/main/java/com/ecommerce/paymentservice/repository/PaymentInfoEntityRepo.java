package com.ecommerce.paymentservice.repository;

import com.ecommerce.paymentservice.entity.PaymentInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentInfoEntityRepo extends JpaRepository<PaymentInfoEntity, Long> {
}
