package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.entity.OrderEntity;
import com.ecommerce.orderservice.entity.OutboxEventEntity;
import com.ecommerce.orderservice.model.Order;
import com.ecommerce.orderservice.model.OrderStatus;
import com.ecommerce.orderservice.repository.OrderRepository;
import com.ecommerce.orderservice.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class OrderEventProducerService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OutboxEventRepository outboxEventRepository;

    @Autowired
    ObjectMapper objectMapper;


    public boolean storeOrder(Order order){
        try {
            OrderEntity orderEntity = saveOrderEntity(order);
            saveOrderCreatedEvent(orderEntity);
        }catch(Exception ex){
            log.info("Exception occurred");
            log.info(ex.getMessage());
            return false;
        }
        return true;
    }

    private OrderEntity saveOrderEntity(Order order) {
        OrderEntity orderEntity = new OrderEntity(order.getUserId(), order.getTotalCost(), order.getCreatedAt(), OrderStatus.PENDING);
        orderRepository.save(orderEntity);
        return orderEntity;
    }

    private void saveOrderCreatedEvent(OrderEntity orderEntity) throws JsonProcessingException {
        OutboxEventEntity outboxEventEntity = new OutboxEventEntity();
            outboxEventEntity.setAggregateId(Long.valueOf(orderEntity.getOrderId()));
        outboxEventEntity.setPayload(objectMapper.writeValueAsString(orderEntity));
        outboxEventEntity.setProcessed(false);
        outboxEventEntity.setCreatedAt(LocalDateTime.now());
        outboxEventEntity.setEventType("Order_Created");

        outboxEventRepository.save(outboxEventEntity);
    }
}
