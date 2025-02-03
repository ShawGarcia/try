package com.shop.payment.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("payment")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentPo {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String orderId;
    private String transactionId;
    private BigDecimal amount;
    private LocalDateTime payAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
