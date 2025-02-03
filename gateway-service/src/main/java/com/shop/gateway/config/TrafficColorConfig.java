package com.shop.gateway.config;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.net.util.SubnetUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
@ConfigurationProperties(prefix = "traffic.color")
@Data
public class TrafficColorConfig {
    /**
     * IP颜色映射规则，支持CIDR格式
     * "192.168.1.0/24=green",
     */
    private List<String> ipColorRules = new ArrayList<>();

    // 优化后的规则存储结构
    private transient List<CIDRRange> parsedRules = new ArrayList<>();

    @PostConstruct
    public void init() {
        parseRules();
    }

    private void parseRules() {
        parsedRules = ipColorRules.stream()
                .map(rule -> {
                    String[] parts = rule.split("=");
                    return new CIDRRange(parts[0], parts[1]);
                })
                .sorted(Comparator.comparingLong(CIDRRange::getStart).reversed())
                .collect(Collectors.toList());
    }

    // CIDR匹配逻辑
    public Optional<String> matchColor(String ip) {
        try {
            long ipLong = ipToLong(ip);
            return parsedRules.stream()
                    .filter(rule -> rule.contains(ipLong))
                    .map(CIDRRange::getColor)
                    .findFirst();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // IP转长整型
    private static long ipToLong(String ipAddress) {
        String[] octets = ipAddress.split("\\.");
        return (Long.parseLong(octets[0]) << 24) +
                (Long.parseLong(octets[1]) << 16) +
                (Long.parseLong(octets[2]) << 8) +
                Long.parseLong(octets[3]);
    }

    @Data
    @AllArgsConstructor
    private static class CIDRRange {
        private long start;
        private long end;
        private String color;

        CIDRRange(String cidr, String color) {
            SubnetUtils utils = new SubnetUtils(cidr);
            utils.setInclusiveHostCount(true);
            SubnetUtils.SubnetInfo info = utils.getInfo();
            this.start = ipToLong(info.getLowAddress());
            this.end = ipToLong(info.getHighAddress());
            this.color = color;
        }

        boolean contains(long ip) {
            return ip >= start && ip <= end;
        }
    }
}

