package com.shop.common.service.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class RegisterReq implements Serializable {
    private static final long serialVersionUID = 1L;

    private String email;
    private String password;
    private String confirmPassword;
} 