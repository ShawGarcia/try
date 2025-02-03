package com.shop.common.service.product;

import lombok.Data;

@Data
public class ListProductsReq {
    private Integer page;
    private Long pageSize;
    private String categoryName;
} 