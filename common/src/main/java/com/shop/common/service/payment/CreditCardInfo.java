package com.shop.common.service.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@ToString(exclude = "creditCardCvv")  // 不输出敏感信息
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CreditCardInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "卡号不能为空")
    @Pattern(regexp = "^[0-9]{13,19}$", message = "无效的信用卡号")
    private String creditCardNumber;

    @NotNull(message = "CVV不能为空")
    @Min(value = 100, message = "CVV必须是3位数")
    @Max(value = 9999, message = "CVV不能超过4位数")
    private Integer creditCardCvv;

    @NotNull(message = "过期年份不能为空")
    @Min(value = 2024, message = "卡片已过期")
    @Max(value = 2099, message = "无效的过期年份")
    private Integer creditCardExpirationYear;

    @NotNull(message = "过期月份不能为空")
    @Min(value = 1, message = "月份必须在1-12之间")
    @Max(value = 12, message = "月份必须在1-12之间")
    private Integer creditCardExpirationMonth;

    /**
     * 校验卡片是否过期
     * @return true-已过期 false-未过期
     */
    @JsonIgnore // 不序列化此方法
    public boolean isExpired() {
        LocalDate today = LocalDate.now();
        LocalDate cardExpiry = LocalDate.of(
                creditCardExpirationYear,
                creditCardExpirationMonth,
                1
        ).plusMonths(1).minusDays(1);  // 获取到期月的最后一天
        return cardExpiry.isBefore(today);
    }
} 