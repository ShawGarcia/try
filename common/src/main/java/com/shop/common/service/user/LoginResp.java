package com.shop.common.service.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResp implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer userId;
} 