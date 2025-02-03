package com.shop.common.filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.cluster.filter.ClusterFilter;

@Activate(group = CommonConstants.CONSUMER)
public class TrafficTagConsumerFilter implements ClusterFilter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) {
        // 从上下文中获取流量标签
        String trafficTag = RpcContext.getCurrentServiceContext().getAttachment(CommonConstants.TAG_KEY);

        // 设置流量标签到 RpcContext
        invocation.setAttachment(CommonConstants.TAG_KEY, trafficTag);

        // 继续执行调用链
        return invoker.invoke(invocation);
    }
}