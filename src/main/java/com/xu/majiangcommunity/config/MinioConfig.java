package com.xu.majiangcommunity.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("minio")
@Data
public class MinioConfig {
    private String endpoint;
    private String bucketName;
    private String accessKey;
    private String secretKey;
}
