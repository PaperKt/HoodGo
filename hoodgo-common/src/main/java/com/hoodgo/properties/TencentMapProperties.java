package com.hoodgo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "hoodgo.tencent.map")
@Data
public class TencentMapProperties {
    private String key;
    private String shopAddress;
    private double deliveryRadiusMeter;
}
