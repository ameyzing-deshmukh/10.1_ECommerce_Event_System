package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.model.Order;
import com.ecommerce.orderservice.service.OrderEventProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderEventProducerController {

    @Autowired
    private OrderEventProducerService producerService;
    @PostMapping("/placed")
    public ResponseEntity orderPlaced(@RequestBody Order order){

        boolean result = producerService.storeOrder(order);
        if(result)
        return ResponseEntity.ok("Test success");
        else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
