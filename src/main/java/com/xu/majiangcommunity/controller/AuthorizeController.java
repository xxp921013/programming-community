package com.xu.majiangcommunity.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.xu.majiangcommunity.domain.User;
import com.xu.majiangcommunity.dto.GithubTokenDTO;
import com.xu.majiangcommunity.dto.GithubUserDTO;
import com.xu.majiangcommunity.provider.GithubPro;
import com.xu.majiangcommunity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@EnableConfigurationProperties(GithubTokenDTO.class)
public class AuthorizeController {
    @Autowired
    private GithubPro githubPro;
    @Autowired
    private GithubTokenDTO githubTokenDTO;

    @Autowired
    private UserService userService;

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code,
                           @RequestParam("state") String state, Model model, HttpServletRequest req, HttpServletResponse resp) {
//        GithubTokenDTO githubTokenDTO = new GithubTokenDTO();
        githubTokenDTO.setCode(code);
        githubTokenDTO.setState(state);
//        githubTokenDTO.setRedirect_uri("http://localhost:8080/callback");
//        githubTokenDTO.setClient_id("96f0a816bfa0bf266e06");
//        githubTokenDTO.setClient_secret("e41f87cef4fd75708c7ddbd2302c5eced3cca96e");
        try {
            String accessToken = githubPro.getAccessToken(githubTokenDTO);
            GithubUserDTO user = githubPro.getUser(accessToken);
            if (user != null) {
                List<User> users = userService.findByAccountId(user.getId());
                if (CollectionUtil.isNotEmpty(users)) {
                    resp.addCookie(new Cookie("token", users.get(0).getToken()));
                } else {
                    User mainUser = new User();
                    String token = String.valueOf(UUID.randomUUID());
                    //ctrl+shift+v提升变量
                    mainUser.setToken(token);
                    mainUser.setImage(user.getAvatar_url());
                    mainUser.setName(user.getName());
                    mainUser.setGmtCreate(DateUtil.current(false));
                    mainUser.setGmtModified(DateUtil.current(false));
                    mainUser.setAccountId(String.valueOf(user.getId()));
                    userService.addUser(mainUser);
//                HttpSession session = req.getSession();
//                session.setAttribute("user", user);
                    resp.addCookie(new Cookie("token", token));
                }

                return "redirect:/";
            } else {
                return "redirect:/";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "index";
        }
    }
}
