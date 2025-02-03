package com.shop.common.service.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginReq implements Serializable {
    private static final long serialVersionUID = 1L;

    private String email;
    private String password;
} 