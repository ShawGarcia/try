package com.shop.common.service.order;

import lombok.Data;

import java.util.List;

@Data
public class ListOrderResp {
    private List<Order> orders;
} 