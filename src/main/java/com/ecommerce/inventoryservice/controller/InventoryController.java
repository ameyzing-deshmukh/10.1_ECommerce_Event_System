package com.ecommerce.inventoryservice.controller;

import com.ecommerce.inventoryservice.entity.ItemInventoryEntity;
import com.ecommerce.inventoryservice.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/addtoinventory")
    public ResponseEntity addToInventory(@RequestBody List<ItemInventoryEntity> itemInventoryList) {
        int count = inventoryService.addToInventory(itemInventoryList);
        return ResponseEntity.ok("Items inventory is updated. Latest count is: " + count);
    }
}
