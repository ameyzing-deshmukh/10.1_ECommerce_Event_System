package com.ecommerce.inventoryservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "item_inventory")
@Getter
@Setter
public class ItemInventoryEntity {

    @Id
    private String itemId;

    private int count;
}
