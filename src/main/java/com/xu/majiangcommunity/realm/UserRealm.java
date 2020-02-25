package com.xu.majiangcommunity.realm;

import com.sun.xml.internal.ws.client.ResponseContext;
import com.xu.majiangcommunity.domain.User;
import com.xu.majiangcommunity.domain.UserExample;
import com.xu.majiangcommunity.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class UserRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    @Override
    public void setName(String name) {
        super.setName("userRealm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        //1.获取登录的用户名密码（token）
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
        String username = upToken.getUsername();
        String password = new String(upToken.getPassword());
        //2.根据用户名查询数据库
        UserExample userExample = new UserExample();
        userExample.or().andNameEqualTo(username);
        List<User> users = userService.selectByExample(userExample);
        User user = users.get(0);
        //3.判断用户是否存在或者密码是否一致
        if (user != null && user.getAccountId().equals(password)) {
            //4.如果一致返回安全数据
            //构造方法：安全数据，密码，realm域名
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getAccountId(), this.getName());
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getResponse();
            Cookie token = new Cookie("token", user.getToken());
            token.setPath("/");
            response.addCookie(token);
            return info;
        }
        //5.不一致，返回null（抛出异常）
        return null;
    }
}
