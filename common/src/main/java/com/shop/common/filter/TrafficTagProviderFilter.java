package com.shop.common.filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.RpcContext;

@Activate(group = CommonConstants.PROVIDER)
public class TrafficTagProviderFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker,
                         Invocation invocation) throws RpcException {
        String attachment = invocation.getAttachment(CommonConstants.TAG_KEY);
        RpcContext.getCurrentServiceContext().setAttachment(CommonConstants.TAG_KEY, attachment);
        return invoker.invoke(invocation);
    }

}