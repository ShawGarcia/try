package com.shop.common.service.order;

import lombok.Data;

import java.util.List;

@Data
public class PlaceOrderReq {
    private Long userId;
    private String userCurrency;
    private Address address;
    private String email;
    private List<OrderItem> orderItems;
} 