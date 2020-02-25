package com.xu.majiangcommunity.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("mj.upload")
@Data
public class MjConfig {
    private List<String> agreeTypes;
}
