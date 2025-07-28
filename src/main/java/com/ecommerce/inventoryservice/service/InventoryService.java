package com.ecommerce.inventoryservice.service;

import com.ecommerce.common.events.AbstractEvent;
import com.ecommerce.inventoryservice.entity.ItemInventoryEntity;
import com.ecommerce.inventoryservice.events.InventoryFailedEvent;
import com.ecommerce.inventoryservice.events.InventoryReservedEvent;
import com.ecommerce.inventoryservice.producer.InventoryProducer;
import com.ecommerce.inventoryservice.repository.ItemInventoryRepo;
import com.ecommerce.orderservice.entity.OrderEntity;
import com.ecommerce.orderservice.events.OrderPlacedEvent;
import com.ecommerce.orderservice.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InventoryService {

    @Autowired
    private ItemInventoryRepo itemInventoryRepo;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InventoryProducer inventoryProducer;

    public void checkInventory(String orderPlacedEvent) {
        OrderPlacedEvent orderPlacedEventObj = getOrderPlacedEventObj(orderPlacedEvent);
        boolean isInventoryToReserve = isInventoryToReserve(orderPlacedEventObj);
        if (isInventoryToReserve) {
            //publish Inventory reserved event
            InventoryReservedEvent inventoryReservedEvent = new InventoryReservedEvent(Long.valueOf(orderPlacedEventObj.getOrderId()), orderPlacedEventObj.getUserId());
            reservedInventory(getMessage(inventoryReservedEvent));
            log.info("++++++++Inventory reserved event is published");
        } else {
            //publish Inventory failed event
            InventoryFailedEvent inventoryFailedEvent = new InventoryFailedEvent(Long.valueOf(orderPlacedEventObj.getOrderId()), orderPlacedEventObj.getUserId());
            failedInventory(getMessage(inventoryFailedEvent));
            log.info("--------Failed inventory event is published");
        }
    }

    private String getMessage(AbstractEvent inventoryReservedEvent) {
        try {
            return objectMapper.writeValueAsString(inventoryReservedEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    //if inventory count is less than order count for any of the item then return false and publish inventory failed event
    //if inventory count is greater than order count for any of the item then return true and publish inventory Reserved event
    private boolean isInventoryToReserve(OrderPlacedEvent orderPlacedEventObj) {
        Map<String, Integer> itemsCountMap = orderPlacedEventObj.getItemsCountMap();
        Set<String> itemIds = itemsCountMap.keySet();
        List<ItemInventoryEntity> itemList = itemInventoryRepo.findAllById(itemIds);

        if (itemList.size() < itemIds.size()) {
            return false;
        }
        for (ItemInventoryEntity itemInventory : itemList) {
            if (itemInventory.getCount() < itemsCountMap.get(itemInventory.getItemId())) {
                return false;
            }
        }

        return true;
    }

    private OrderPlacedEvent getOrderPlacedEventObj(String orderPlacedEvent) {
        OrderPlacedEvent orderPlacedEventObj = null;
        try {
            orderPlacedEventObj = objectMapper.readValue(orderPlacedEvent, OrderPlacedEvent.class);
        } catch (JsonProcessingException e) {
            log.info("Json parsing error: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return orderPlacedEventObj;
    }

    public void updateInventory(String inventoryReservedEvent) {
        //Reduce the count in InventoryEntity table;
        InventoryReservedEvent inventoryReservedEventObj = null;
        try {
            inventoryReservedEventObj = objectMapper.readValue(inventoryReservedEvent, InventoryReservedEvent.class);
            Optional<OrderEntity> orderEntity = orderRepository.findById(inventoryReservedEventObj.getOrderId());
            if (orderEntity.isPresent()) {
                Map<String, Integer> itemCountMap = orderEntity.get().getItemsCountMap();
                Set<String> itemIds = itemCountMap.keySet();
                List<ItemInventoryEntity> inventoryItems = itemInventoryRepo.findAllById(itemIds);
                for (ItemInventoryEntity item : inventoryItems) {
                    int originalCount = item.getCount();
                    int orderCount = itemCountMap.get(item.getItemId());
                    item.setCount(originalCount - orderCount);
                }
                itemInventoryRepo.saveAll(inventoryItems);
            }
        } catch (JsonProcessingException e) {
            log.info("Json parsing error: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void failedInventory(String message) {
        inventoryProducer.publishToInventoryFailed(message);
    }

    public void reservedInventory(String message) {
        inventoryProducer.publishToInventoryReserved(message);
    }

    //Update count of each item to 2
    public int addToInventory(List<ItemInventoryEntity> itemInventoryList) {

        List<String> itemIds = itemInventoryList.stream().map(i -> i.getItemId()).collect(Collectors.toList());

        List<ItemInventoryEntity> entitiesToUpdate = itemInventoryRepo.findAllById(itemIds);
        entitiesToUpdate.forEach(entity -> entity.setCount(2));
        for (String itemId : itemIds)
            entitiesToUpdate.add(new ItemInventoryEntity(itemId, 2));
        itemInventoryRepo.saveAll(entitiesToUpdate);
        return entitiesToUpdate.size();
    }
}
