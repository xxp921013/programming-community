package com.xu.majiangcommunity.interceptor;

import cn.hutool.core.util.StrUtil;
import com.xu.majiangcommunity.domain.User;
import com.xu.majiangcommunity.service.impl.UserService;
import com.xu.majiangcommunity.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptors implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("拦截器生效");
        String token = CookieUtils.getCookieValue(request, "token");
        if (StrUtil.isNotBlank(token)) {
            User byToken = userService.findByToken(token);
            if (byToken != null) {
                request.getSession().setAttribute("user", byToken);
            }
        } else {
            request.getSession().setAttribute("error", "您未登录");
            response.sendRedirect("/");
            return false;
        }
        return true;
    }
}
