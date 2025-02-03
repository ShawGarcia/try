package com.shop.gateway.filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.cluster.filter.ClusterFilter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Activate(group = CommonConstants.CONSUMER)
public class TrafficTagConsumerFilter implements ClusterFilter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String trafficTag = null;
        if (attributes != null) {
            trafficTag = (String)attributes.getRequest().getAttribute(CommonConstants.TAG_KEY);
        }

        // 设置流量标签到 RpcContext
        invocation.setAttachment(CommonConstants.TAG_KEY, trafficTag);

        // 继续执行调用链
        return invoker.invoke(invocation);
    }
}
