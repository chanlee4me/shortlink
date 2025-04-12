package com.chanlee.shortlink.admin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "shortlink.jwt")
@Data
public class JwtConfiguration {
    private String SecretKey;
    private long Ttl;
    private String TokenName;
}
