package com.alumni.framework.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "tencent.map")
public class TencentMapProperties {

    private String key;
    private String geocoderUrl;
}
