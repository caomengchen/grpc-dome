package com.ctd.bank.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 统一认证配置文件
 */
@Component
@Data
@ConfigurationProperties(prefix = "dzzz", ignoreUnknownFields = false)
public class DzkConfig {

    private String webserviceUrl;

    private String sysCode;

}
