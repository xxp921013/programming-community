package com.xu.majiangcommunity.config;

import com.xu.majiangcommunity.interceptor.AuthInterceptors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Autowired
    private AuthInterceptors authInterceptors;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptors).addPathPatterns("/userArticle").addPathPatterns("/addArticle").addPathPatterns("/publish").addPathPatterns("/**/userDetail");
    }
}
