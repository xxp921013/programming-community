package com.xu.majiangcommunity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@MapperScan("com.xu.majiangcommunity.dao")
@EnableCaching
@EnableRedisHttpSession
public class MajiangCommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(MajiangCommunityApplication.class, args);
    }

}
