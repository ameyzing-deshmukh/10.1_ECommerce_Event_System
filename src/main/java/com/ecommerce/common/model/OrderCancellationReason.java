package com.ecommerce.common.model;

public enum OrderCancellationReason {
    PAYMENT_FAILED,
    INVENTORY_SHORTAGE,
    INVALID_ORDER,
    CANCELLED_BY_USER,
    PAYMENT_WINDOW_EXPIRED
}
