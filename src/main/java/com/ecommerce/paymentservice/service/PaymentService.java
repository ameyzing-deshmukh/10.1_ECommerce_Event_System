package com.ecommerce.paymentservice.service;

import com.ecommerce.inventoryservice.events.InventoryReservedEvent;
import com.ecommerce.orderservice.entity.OrderEntity;
import com.ecommerce.orderservice.repository.OrderRepository;
import com.ecommerce.paymentservice.entity.PaymentInfoEntity;
import com.ecommerce.paymentservice.model.PaymentStatus;
import com.ecommerce.paymentservice.repository.PaymentInfoEntityRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class PaymentService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PaymentInfoEntityRepo paymentInfoEntityRepo;

    @Autowired
    private OrderRepository orderRepository;

    InventoryReservedEvent reservedEvent;

    public void validateAndInitiatePaymentProcess(String inventoryReservedEvent) {
        extractInventoryReservedEvent(inventoryReservedEvent);
        validateIncomingEventAndInitiatePayment(inventoryReservedEvent);
    }

    public void validateIncomingEventAndInitiatePayment(String inventoryReservedEvent) {
        boolean isValidEvent;
        if (reservedEvent.getOrderId() != 0 && StringUtils.isNotBlank(reservedEvent.getUserId())) {
            isValidEvent = true;
        } else {
            log.error("Inventory Reserved Event is having incomplete data: {}", inventoryReservedEvent);
            isValidEvent = false;
        }
        initiatePaymentProcess(isValidEvent);
    }

    public void extractInventoryReservedEvent(String inventoryReservedEvent) {
        try {
            reservedEvent = objectMapper.readValue(inventoryReservedEvent, InventoryReservedEvent.class);
        } catch (JsonProcessingException e) {
            log.error("Error occurred in getting InventoryReservedEvent. Message: {}", inventoryReservedEvent);
            log.error("Error message: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void initiatePaymentProcess(boolean isValidEvent) {
        if (isValidEvent) {
            boolean isPaymentSuccessful = initiatePayment();
            if (isPaymentSuccessful) {
                publishPaymentConfirmedEvents();
            } else {
                publishPaymentFailedEvents();
            }
        } else {
            publishInvalidRequestEvents();
        }
    }

    public boolean initiatePayment() {

        PaymentInfoEntity paymentInfoEntity = buildPaymentInfoEntity();
        boolean paymentResult = completePayment(paymentInfoEntity);
        savePaymentInfo(paymentInfoEntity, paymentResult);
        return paymentResult;
    }

    private boolean completePayment(PaymentInfoEntity paymentInfoEntity) {
        //ToDo: Need actual payment gateway logic here.
        if (reservedEvent.getUserId().equals("Pranali101")) {
            paymentInfoEntity.setPaymentTs(LocalDateTime.now());
            return true;
        } else {
            return false;
        }
    }

    private void savePaymentInfo(PaymentInfoEntity paymentInfoEntity, boolean paymentResult) {
        if (paymentResult) {
            paymentInfoEntity.setPaymentStatus(PaymentStatus.PAYMENT_SUCCESSFUL);
        } else {
            paymentInfoEntity.setPaymentStatus(PaymentStatus.PAYMENT_FAILED);
        }
        log.info("Time - {}", paymentInfoEntity.getPaymentTs());
        paymentInfoEntityRepo.save(paymentInfoEntity);
    }

    private PaymentInfoEntity buildPaymentInfoEntity() {
        PaymentInfoEntity paymentInfoEntity = new PaymentInfoEntity();
        Optional<OrderEntity> orderEntity = orderRepository.findById(reservedEvent.getOrderId());
        if (orderEntity.isPresent()) {
            paymentInfoEntity.setOrderId(orderEntity.get().getOrderId());
            paymentInfoEntity.setUserId(orderEntity.get().getUserId());
            paymentInfoEntity.setPaymentId("Dummy" + orderEntity.get().getOrderId());
            paymentInfoEntity.setPaymentAmount(orderEntity.get().getTotalCost());
        } else {
            log.info("Order details not found");
        }
        return paymentInfoEntity;
    }

    public void publishPaymentConfirmedEvents() {
        log.info("++++++++Payment Confirmed Events are published. ");
        publishPaymentConfirmedEvent("publishPaymentConfirmedEvent");
    }

    public void publishPaymentFailedEvents() {
        log.info("--------Payment Failed Events are published. ");
        publishPaymentFailedEvent("publishPaymentFailedEvent");
        publishOrderCancelledEvent("publishOrderCancelledEvent");
        publishInventoryReleasedEvent("publishInventoryReleasedEvent");
    }

    public void publishPaymentFailedEvent(String message) {
        log.info("--------Payment Failed Event is published. {}", message);
    }

    public void publishOrderCancelledEvent(String message) {
        log.info("--------Order Cancelled Event is published. {}", message);
    }

    public void publishInventoryReleasedEvent(String message) {
        log.info("--------Inventory Released Event is published. {}", message);
    }

    public void publishPaymentConfirmedEvent(String message) {
        log.info("++++++++Payment Confirmed Event is published. {}", message);
    }

    public void publishInvalidRequestEvents() {
        log.info("--------Invalid Request Events are published. ");
        publishOrderCancelledEvent("publishOrderCancelledEvent");
        publishInventoryReleasedEvent("publishInventoryReleasedEvent");
    }
}
