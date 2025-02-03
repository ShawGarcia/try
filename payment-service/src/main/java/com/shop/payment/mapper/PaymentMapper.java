package com.shop.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.payment.entity.po.PaymentPo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper extends BaseMapper<PaymentPo> {
}
