package com.ecommerce.inventoryservice.services;

import com.ecommerce.inventoryservice.entity.ItemInventoryEntity;
import com.ecommerce.inventoryservice.repository.ItemInventoryRepo;
import com.ecommerce.orderservice.events.OrderPlacedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InventoryService {

    @Autowired
    private ItemInventoryRepo itemInventoryRepo;

    @Autowired
    private ObjectMapper objectMapper;

    public boolean checkInventory(String orderPlacedEvent) throws JsonProcessingException {
        OrderPlacedEvent orderPlacedEventObj = null;
        try {
            orderPlacedEventObj = objectMapper.readValue(orderPlacedEvent, OrderPlacedEvent.class);
        } catch (JsonProcessingException e) {
            log.info("Json parsing error: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        Map<String, Integer> itemsCountMap = orderPlacedEventObj.getItemsCountMap();
        Set<String> itemIds = itemsCountMap.keySet();
        //if inventory count is less than order count for any of the item then return false and publish inventory failed event
        //if inventory count is greater than order count for any of the item then return true and publish inventory Reserved event

        List<ItemInventoryEntity> itemList = itemInventoryRepo.findAllById(itemIds);
        if (itemList.size() < itemIds.size()) {
            log.info("--------Failed inventory event is published");
            return false;
        }
        for (ItemInventoryEntity itemInventory : itemList) {
            if (itemInventory.getCount() < itemsCountMap.get(itemInventory.getItemId())) {
                //publish Inventory failed event
                log.info("--------Failed inventory event is published");
                return false;
            }
        }
        //publish Inventory reserved event
        log.info("++++++++Inventory reserved event is published");

        return true;
    }

    public void updateInventory() {

    }

    public void failedInventory() {

    }

    public void reservedInventory() {

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
