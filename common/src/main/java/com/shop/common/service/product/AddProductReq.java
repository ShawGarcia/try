package com.shop.common.service.product;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AddProductReq {
    private Long id;
    private String name;
    private String description;
    private String picture;
    private Double price;
    private List<String> categories;
} 