package com.ecommerce.paymentservice.service;

import com.ecommerce.common.events.InventoryReleasedEvent;
import com.ecommerce.common.events.OrderCancelledEvent;
import com.ecommerce.common.model.InventoryReleaseReason;
import com.ecommerce.common.model.OrderCancellationReason;
import com.ecommerce.common.producer.CommonProducer;
import com.ecommerce.common.util.CommonUtil;
import com.ecommerce.paymentservice.events.PaymentConfirmedEvent;
import com.ecommerce.paymentservice.events.PaymentFailedEvent;
import com.ecommerce.paymentservice.model.PaymentFailedReason;
import com.ecommerce.paymentservice.producer.PaymentEventProducer;
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

    @Autowired
    private PaymentEventProducer paymentEventProducer;

    @Autowired
    private CommonProducer commonProducer;

    @Autowired
    private CommonUtil commonUtil;

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
            PaymentInfoEntity paymentInfo = initiatePayment();
            if (StringUtils.isNotBlank(paymentInfo.getPaymentId()) && (paymentInfo.getPaymentStatus().equals(PaymentStatus.PAYMENT_SUCCESSFUL) || paymentInfo.getPaymentStatus().equals(PaymentStatus.CASH_ON_DELIVERY))) {
                publishPaymentConfirmedEvents(paymentInfo.getPaymentId());
            } else {
                publishPaymentFailedEvents();
            }
        } else {
            publishInvalidOrderEvents();
        }
    }

    public PaymentInfoEntity initiatePayment() {

        PaymentInfoEntity paymentInfoEntity = buildPaymentInfoEntity();
        completePayment(paymentInfoEntity);
        savePaymentInfo(paymentInfoEntity);
        return paymentInfoEntity;
    }

    private void completePayment(PaymentInfoEntity paymentInfoEntity) {
        //ToDo: Need actual payment gateway logic here.
        if (reservedEvent.getUserId().equals("Pranali101")) {
            paymentInfoEntity.setPaymentTs(LocalDateTime.now());
            paymentInfoEntity.setPaymentStatus(PaymentStatus.PAYMENT_SUCCESSFUL);
        } else if (reservedEvent.getUserId().equals("Pranali102")) {
            paymentInfoEntity.setPaymentStatus(PaymentStatus.CASH_ON_DELIVERY);
        } else {
            paymentInfoEntity.setPaymentStatus(PaymentStatus.PAYMENT_FAILED);
        }
    }

    private void savePaymentInfo(PaymentInfoEntity paymentInfoEntity) {
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

    public void publishInvalidOrderEvents() {
        log.info("--------Invalid Request Events are published. ");
        publishOrderCancelledEvent(OrderCancellationReason.INVALID_ORDER);
        publishInventoryReleasedEvent(InventoryReleaseReason.INVALID_ORDER);
    }

    public void publishPaymentConfirmedEvents(String paymentId) {
        log.info("++++++++Payment Confirmed Events are published. ");
        publishPaymentConfirmedEvent(paymentId);
    }

    public void publishPaymentFailedEvents() {
        log.info("--------Payment Failed Events are published. ");
        publishPaymentFailedEvent(PaymentFailedReason.MERCHANT_SERVER_DOWN);
        publishOrderCancelledEvent(OrderCancellationReason.PAYMENT_FAILED);
        publishInventoryReleasedEvent(InventoryReleaseReason.PAYMENT_FAILED);
    }

    public void publishPaymentConfirmedEvent(String paymentId) {
        PaymentConfirmedEvent event = new PaymentConfirmedEvent(reservedEvent.getOrderId(), reservedEvent.getUserId(), paymentId);
        log.info("++++++++Payment Confirmed Event is published. {}", commonUtil.getMessage(event));
        paymentEventProducer.publishPaymentConfirmedEvent(commonUtil.getMessage(event));
    }

    public void publishPaymentFailedEvent(PaymentFailedReason reason) {
        PaymentFailedEvent event = new PaymentFailedEvent(reservedEvent.getOrderId(), reservedEvent.getUserId(), reason);
        log.info("--------Payment Failed Event is published. {}", commonUtil.getMessage(event));
        paymentEventProducer.publishPaymentFailedEvent(commonUtil.getMessage(event));
    }

    public void publishOrderCancelledEvent(OrderCancellationReason reason) {
        OrderCancelledEvent orderCancelledEvent = new OrderCancelledEvent(reservedEvent.getOrderId(), reservedEvent.getUserId(), reason);
        log.info("--------Order Cancelled Event is published. {}", commonUtil.getMessage(orderCancelledEvent));
        commonProducer.publishOrderCancelledEvent(commonUtil.getMessage(orderCancelledEvent));
    }

    public void publishInventoryReleasedEvent(InventoryReleaseReason reason) {
        InventoryReleasedEvent inventoryReleasedEvent = new InventoryReleasedEvent(reservedEvent.getOrderId(), reservedEvent.getUserId(), reason);
        log.info("--------Inventory Released Event is published. {}", commonUtil.getMessage(inventoryReleasedEvent));
        commonProducer.publishInventoryReleasedEvent(commonUtil.getMessage(inventoryReleasedEvent));
    }


}
