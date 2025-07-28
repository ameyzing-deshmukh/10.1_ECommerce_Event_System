package com.ecommerce.inventoryservice.data;

import com.ecommerce.inventoryservice.entity.ItemInventoryEntity;
import com.ecommerce.inventoryservice.repository.ItemInventoryRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class InventoryDataSeeder implements CommandLineRunner {

    private final ItemInventoryRepo inventoryRepository;

    public InventoryDataSeeder(ItemInventoryRepo inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (inventoryRepository.count() == 0) {
            ItemInventoryEntity item1 = new ItemInventoryEntity("Item1", 100);
            ItemInventoryEntity item2 = new ItemInventoryEntity("Item2", 150);

            inventoryRepository.saveAll(List.of(item1, item2));
            log.info("✅ Seeded inventory data.");
        }
    }
}
