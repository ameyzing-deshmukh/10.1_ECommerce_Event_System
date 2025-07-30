package com.ecommerce.shippingdeliveryservice.service;

import com.ecommerce.common.util.CommonUtil;
import com.ecommerce.paymentservice.events.PaymentConfirmedEvent;
import com.ecommerce.shippingdeliveryservice.producer.ShippingProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        publishShipmentEvent();
    }

    private void publishShipmentEvent() {

        String message = commonUtil.getMessage(event);
        shippingProducer.publishShippingInitiatedEvent(message);
    }

    private boolean validateEvent() {
        if (StringUtils.isNotBlank(event.getPaymentId())) {
            return true;
        } else {
            return false;
        }
    }

    private void extractObject(String paymentConfirmedEvent) {
        try {
            event = objectMapper.readValue(paymentConfirmedEvent, PaymentConfirmedEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
