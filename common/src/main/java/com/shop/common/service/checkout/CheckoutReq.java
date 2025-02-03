package com.shop.common.service.checkout;

import com.shop.common.service.order.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckoutReq {

    @NotNull(message = "用户ID不能为空")
    @Positive(message = "用户ID必须为正数")
    private Long userId;

    @NotBlank(message = "名字不能为空")
    private String firstname;

    @NotBlank(message = "姓氏不能为空")
    private String lastname;

    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "邮箱格式不正确")
    private String email;

    @NotNull(message = "地址不能为空")
    private Address address;

    @NotNull(message = "信用卡信息不能为空")
    private com.shop.common.service.payment.CreditCardInfo creditCard;
} 