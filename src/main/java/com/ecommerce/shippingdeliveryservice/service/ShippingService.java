package com.ecommerce.shippingdeliveryservice.service;

import com.ecommerce.common.events.AbstractEvent;
import com.ecommerce.shippingdeliveryservice.events.ShippingCompletedEvent;
import com.ecommerce.shippingdeliveryservice.events.ShippingInitiatedEvent;
import com.ecommerce.common.util.CommonUtil;
import com.ecommerce.paymentservice.events.PaymentConfirmedEvent;
import com.ecommerce.shippingdeliveryservice.entity.ShipmentInfoEntity;
import com.ecommerce.shippingdeliveryservice.model.ShippingStatus;
import com.ecommerce.shippingdeliveryservice.repository.ShipmentInfoRepository;
import com.ecommerce.shippingdeliveryservice.producer.ShippingProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ShippingService {

    private PaymentConfirmedEvent paymentConfirmedEventObj;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ShippingProducer shippingProducer;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private ShipmentInfoRepository shippingRepo;

    public void processShippingEvent(String paymentConfirmedEvent) {
        log.info("Validate paymentConfirmedEvent");
        extractObject(paymentConfirmedEvent);
        boolean validEvent = validateEvent();
        if (validEvent) {
            initiateShipping();
        }
    }

    private void initiateShipping() {
        //ToDo: Need to implement real shipping logic
        persistShipmentInfo();
    }

    private void persistShipmentInfo() {
        ShipmentInfoEntity entity = new ShipmentInfoEntity("shipment" + paymentConfirmedEventObj.getOrderId(), paymentConfirmedEventObj.getUserId(), "DTDC", "Kharadi", LocalDateTime.now(), ShippingStatus.INITIATED, LocalDate.now().plusDays(10), paymentConfirmedEventObj.getOrderId(), paymentConfirmedEventObj.getUserId());
        shippingRepo.save(entity);
        publishShipmentInitiatedEvent(entity);
    }

    private void publishShipmentInitiatedEvent(ShipmentInfoEntity entity) {
        ShippingInitiatedEvent shippingInitiatedEvent = new ShippingInitiatedEvent(paymentConfirmedEventObj.getOrderId(), paymentConfirmedEventObj.getUserId(), entity.getShippingId(), entity.getShippingPartnerName(), entity.getShippingAddress(), entity.getShippingDate(), entity.getStatus());
        shippingProducer.publishShippingInitiatedEvent(commonUtil.getMessage(shippingInitiatedEvent));
    }

    private boolean validateEvent() {
        return StringUtils.isNotBlank(paymentConfirmedEventObj.getPaymentId());
    }

    private void extractObject(String paymentConfirmedEvent) {
        try {
            paymentConfirmedEventObj = objectMapper.readValue(paymentConfirmedEvent, PaymentConfirmedEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(fixedRate = 20000)
    public void simulateShipmentCompletion() {
        List<ShipmentInfoEntity> initiatedShipments = shippingRepo.findAllByStatus(ShippingStatus.INITIATED);
        ShippingCompletedEvent shippingCompletedEvent;
        if (initiatedShipments.size() > 0) {
            for (ShipmentInfoEntity shipment : initiatedShipments) {
                shipment.setStatus(ShippingStatus.COMPLETED);
                //ToDo: Join order and shipment tables
                shippingCompletedEvent = new ShippingCompletedEvent(shipment.getOrderId(), shipment.getUserId(), shipment.getShippingId(), shipment.getShippingPartnerName(), shipment.getShippingAddress(), shipment.getShippingDate(), shipment.getStatus());
                shippingProducer.publishShippingCompletedEvent(createShippingCompletionEvent(shippingCompletedEvent));
            }
            log.info("Shipment with ids {} is completed now.", initiatedShipments.stream().map(s -> s.getShippingId()).collect(Collectors.joining(", ")));
            shippingRepo.saveAll(initiatedShipments);
        } else {
            log.info("No shipment is initiated yet.");
        }

    }

    private String createShippingCompletionEvent(AbstractEvent event) {
        return commonUtil.getMessage(event);
    }
}
