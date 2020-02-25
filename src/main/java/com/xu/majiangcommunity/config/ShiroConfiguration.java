package com.xu.majiangcommunity.config;

import com.xu.majiangcommunity.realm.UserRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {

    //1.创建realm
    @Bean
    public UserRealm getRealm() {
        return new UserRealm();
    }

    //2.创建安全管理器
    @Bean
    public SecurityManager getSecurityManager(UserRealm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        return securityManager;
    }

    //3.配置shiro的过滤器工厂

    /**
     * 再web程序中，shiro进行权限控制全部是通过一组过滤器集合进行控制
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        //1.创建过滤器工厂
        ShiroFilterFactoryBean filterFactory = new ShiroFilterFactoryBean();
        //2.设置安全管理器
        filterFactory.setSecurityManager(securityManager);
        //3.通用配置（跳转登录页面，为授权跳转的页面）
        filterFactory.setLoginUrl("/localUser/login");//跳转url地址
        filterFactory.setUnauthorizedUrl("/404.html");//未授权的url
        //4.设置过滤器集合
        /**
         * 设置所有的过滤器：有顺序map
         *     key = 拦截的url地址
         *     value = 过滤器类型
         * anon:无需认证可以访问
         * authc:必须认证才能访问
         * user:必须拥有记住我才能访问
         * role:必须拥有某个角色才能访问
         * perms:拥有对某个资源的权限才能访问
         */
        Map<String, String> filterMap = new LinkedHashMap<>();
        //filterMap.put("/user/home","anon");//当前请求地址可以匿名访问

        //具有某中权限才能访问
        //使用过滤器的形式配置请求地址的依赖权限
        //filterMap.put("/user/home","perms[user-home]"); //不具备指定的权限，跳转到setUnauthorizedUrl地址

        //使用过滤器的形式配置请求地址的依赖角色
        //filterMap.put("/user/home","roles[系统管理员]");

        filterMap.put("/publish/**", "authc");//当前请求地址必须认证之后可以访问
        filterMap.put("/images/**", "anon");//当前请求地址必须认证之后可以访问
        filterMap.put("/fonts/**", "anon");//当前请求地址必须认证之后可以访问
        filterMap.put("/js/**", "anon");//当前请求地址必须认证之后可以访问
        filterMap.put("/css/**", "anon");//当前请求地址必须认证之后可以访问
        filterMap.put("/userLocal/login", "anon");//当前请求地址必须认证之后可以访问
        filterFactory.setFilterChainDefinitionMap(filterMap);

        return filterFactory;
    }


    //开启对shior注解的支持
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
