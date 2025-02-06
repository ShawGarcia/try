package com.shop.common.service.product;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchProductsResp {
    private List<AddProductReq> results;
} 