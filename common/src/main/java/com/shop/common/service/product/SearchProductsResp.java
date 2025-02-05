package com.shop.common.service.product;

import lombok.Data;

import java.util.List;

@Data
public class SearchProductsResp {
    private List<AddProductReq> results;
} 