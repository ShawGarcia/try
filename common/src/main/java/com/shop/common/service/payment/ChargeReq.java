package com.shop.common.service.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ChargeReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    private Float amount;

    @NotNull(message = "信用卡信息不能为空")
    private CreditCardInfo creditCard;

    @NotBlank(message = "订单号不能为空")
    @Length(max = 32, message = "订单号长度不能超过32")
    private String orderId;

    @NotNull(message = "用户ID不能为空")
    @Positive(message = "用户ID必须为正数")
    private Long userId;
} 