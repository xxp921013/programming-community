package com.xu.majiangcommunity.config;

import com.xu.majiangcommunity.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private UserInterceptor userInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/localUser/userDetail/**")
                .addPathPatterns("/updateArticle/**")
                .addPathPatterns("/deleteArticle/**")
                .addPathPatterns("/addArticle/**")
                .addPathPatterns("/userArticle/**")
                .addPathPatterns("/myRound/**")
                .addPathPatterns("/addRound/**")
                .addPathPatterns("/localUser/doUploadHead/**")
                .addPathPatterns("/localUser/myCollection/**")
                .addPathPatterns("/localUser/addCollection/**")
                .addPathPatterns("/localUser/removeCollection/**")
                .addPathPatterns("/articleDetail/**")
        ;
    }
}
