package com.ecommerce.orderservice.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemDto {
    private String itemId;
    private String name;
    private BigDecimal rate;
}
