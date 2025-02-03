package com.shop.common.service.order;

import lombok.Data;

@Data
public class Address {
    private String streetAddress;
    private String city;
    private String state;
    private String country;
    private Integer zipCode;
} 