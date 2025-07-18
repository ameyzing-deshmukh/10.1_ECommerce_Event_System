package com.ecommerce.controller;

import com.ecommerce.events.OrderEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderEventProducerController {
    @PostMapping
    public ResponseEntity orderPlaced(@RequestBody OrderEvent orderEvent){
        return ResponseEntity.ok("Test success");
    }
}
