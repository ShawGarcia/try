package com.shop.common.service.order;

import lombok.Data;

@Data
public class OrderItem {
    private Long productId;
    private Integer quantity;
    private Float cost;
} 