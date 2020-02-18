package com.xu.majiangcommunity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xu.majiangcommunity.dao")
public class MajiangCommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(MajiangCommunityApplication.class, args);
    }

}
