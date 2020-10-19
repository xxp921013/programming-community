package com.xu.majiangcommunity.interceptor;

import com.xu.majiangcommunity.UserException;
import com.xu.majiangcommunity.domain.SecurityUser;
import com.xu.majiangcommunity.enums.ExcetionEnmu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class UserInterceptor implements HandlerInterceptor {


    //获得当前线程域(域是map结构)
    private static final ThreadLocal<SecurityUser> tl = new ThreadLocal<>();

    @Override

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SecurityUser user = null;

        try {
            user = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return true;
        }
        //String token = request.getHeader("LY_TOKEN");
        //解析token

        try {
            //传递用户
            //request.setAttribute("user", userInfo);
            //向线程内存入数据
            tl.set(user);

            return true;
        } catch (Exception e) {
            log.error("[购物车]解析用户失败", e);
            //拦截
            return false;
        }
    }

    @Override
    //该方法在视图渲染完之后执行
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //使用完后删除数据
        tl.remove();
    }

    public static SecurityUser getUser() {
        SecurityUser user = tl.get();
//        if (user == null) {
//            throw new UserException(ExcetionEnmu.TEST_THROW);
//        }
        return user;
    }
}
