package com.ecommerce.inventoryservice.repository;

import com.ecommerce.inventoryservice.entity.ItemInventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemInventoryRepo extends JpaRepository<ItemInventoryEntity, String> {

}
