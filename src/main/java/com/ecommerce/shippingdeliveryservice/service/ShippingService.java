package com.ecommerce.shippingdeliveryservice.service;

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

    private PaymentConfirmedEvent event;

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
        publishShipmentEvent();
    }

    private void persistShipmentInfo() {
        shippingRepo.save(new ShipmentInfoEntity("shipment" + event.getOrderId(), event.getUserId(), "DTDC", "Kharadi", LocalDateTime.now(), ShippingStatus.INITIATED, LocalDate.now().plusDays(10)));
    }

    private void publishShipmentEvent() {

        String message = commonUtil.getMessage(event);
        shippingProducer.publishShippingInitiatedEvent(message);
    }

    private boolean validateEvent() {
        return StringUtils.isNotBlank(event.getPaymentId());
    }

    private void extractObject(String paymentConfirmedEvent) {
        try {
            event = objectMapper.readValue(paymentConfirmedEvent, PaymentConfirmedEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(fixedRate = 20000)
    public void simulateShipmentCompletion() {
        List<ShipmentInfoEntity> initiatedShipments = shippingRepo.findAllByStatus(ShippingStatus.INITIATED);

        if (initiatedShipments.size() > 0) {
            for (ShipmentInfoEntity shipment : initiatedShipments) {
                shipment.setStatus(ShippingStatus.COMPLETED);
            }
            log.info("Shipment with ids {} is completed now.", initiatedShipments.stream().map(s -> s.getShippingId()).collect(Collectors.joining(", ")));
            shippingRepo.saveAll(initiatedShipments);
        } else {
            log.info("No shipment is initiated yet.");
        }

    }
}
