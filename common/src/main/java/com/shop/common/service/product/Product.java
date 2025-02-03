package com.shop.common.service.product;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Product {
    private Long id;
    private String name;
    private String description;
    private String picture;
    private Float price;
    private List<String> categories;
} 