package com.ecommerce.common.util;

import com.ecommerce.common.events.AbstractEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommonUtil {

    @Autowired
    private ObjectMapper objectMapper;

    public String getMessage(AbstractEvent inventoryReservedEvent) {
        try {
            return objectMapper.writeValueAsString(inventoryReservedEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
