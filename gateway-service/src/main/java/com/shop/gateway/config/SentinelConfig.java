package com.shop.gateway.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "sentinel.flow")
@Data
public class SentinelConfig {
    
    private List<FlowRuleProperties> rules = new ArrayList<>();
    
    @Data
    public static class FlowRuleProperties {
        private String resource;
        private int grade = RuleConstant.FLOW_GRADE_QPS;
        private double count;
        private int controlBehavior = RuleConstant.CONTROL_BEHAVIOR_DEFAULT;
        private int warmUpPeriodSec;
        private int maxQueueingTimeMs;
    }
    
    @PostConstruct
    public void init() {
        log.info("开始加载 Sentinel 规则配置...");
        log.info("配置的规则数量: {}", rules.size());
        
        if (rules.isEmpty()) {
            log.warn("未找到任何 Sentinel 规则配置！");
            return;
        }
        
        List<FlowRule> flowRules = rules.stream()
            .map(rule -> {
                FlowRule flowRule = new FlowRule();
                flowRule.setResource(rule.getResource());
                flowRule.setGrade(rule.getGrade());
                flowRule.setCount(rule.getCount());
                flowRule.setControlBehavior(rule.getControlBehavior());
                flowRule.setWarmUpPeriodSec(rule.getWarmUpPeriodSec());
                flowRule.setMaxQueueingTimeMs(rule.getMaxQueueingTimeMs());
                
                log.info("创建限流规则: resource={}, count={}, grade={}", 
                    rule.getResource(), rule.getCount(), rule.getGrade());
                    
                return flowRule;
            })
            .toList();
            
        FlowRuleManager.loadRules(flowRules);
        log.info("Sentinel 规则加载完成，共加载 {} 条规则", flowRules.size());
    }
}