package com.xu.majiangcommunity.config;

import cn.hutool.core.date.DateUtil;
import com.xu.majiangcommunity.service.impl.SecurityUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity//启动springsecurity过滤器链
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private SecurityUserServiceImpl userService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    //
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("jack").password(new BCryptPasswordEncoder().encode("123456")).authorities("PRODUCT_ADD");
        auth.userDetailsService(userService).passwordEncoder(encoder);
    }

    @Override
    //
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/localUser/registry").permitAll()
                .antMatchers("/localUser/do_register").permitAll()
                .antMatchers("/localUser/login").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/dist/**").permitAll()
                .antMatchers("/font/**").permitAll()
                .antMatchers("/fonts/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/src/**").permitAll()
                .antMatchers("/articleDetail/**").permitAll()
                .antMatchers("/hotTags/**").permitAll()
                .antMatchers("/recentHot/**").permitAll()
                .antMatchers("/mail/sendRegistryMail/**").permitAll()
                .anyRequest()                    // 其他任意请求
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/localUser/login")
                .loginProcessingUrl("/login")//登录地址,默认login
                .permitAll()
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        response.sendRedirect("/localUser/loginSuccess");
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

                    }
                })
                .and().logout()//登出
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")

//                .permitAll().invalidateHttpSession(true)
//                .deleteCookies("SESSION")
                .and().csrf().disable()
                .rememberMe()
                .tokenValiditySeconds(60 * 60 * 24 * 7)
        //记住我,默认使用cookie,时间两周
        ;
        //异常处理
        http.exceptionHandling().accessDeniedPage("/");
    }
}
