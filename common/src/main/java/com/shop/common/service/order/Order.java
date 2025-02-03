package com.shop.common.service.order;

import lombok.Data;

import java.util.List;

@Data
public class Order {
    private List<OrderItem> orderItems;
    private String orderId;
    private Long userId;
    private String userCurrency;
    private Address address;
    private String email;
    private Long createdAt;
} 