package com.xu.majiangcommunity.controller;

import cn.hutool.core.util.StrUtil;
import com.xu.majiangcommunity.constant.MqConstant;
import com.xu.majiangcommunity.dao.ArticleMapper;
import com.xu.majiangcommunity.domain.Article;
import com.xu.majiangcommunity.domain.ArticleEs;
import com.xu.majiangcommunity.domain.User;
import com.xu.majiangcommunity.dto.ArticleDTO;
import com.xu.majiangcommunity.dto.PageResult;
import com.xu.majiangcommunity.service.ArticleService;
import com.xu.majiangcommunity.service.UserService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Struct;
import java.util.List;

import static org.apache.ibatis.ognl.DynamicSubscript.all;

@Controller
public class IndexController {
    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;
    @Autowired
    private AmqpTemplate at;

    @GetMapping("/")
    public String getIndex(@CookieValue(value = "token", required = false) String token,
                           HttpServletRequest req, Model model,
                           @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                           @RequestParam(value = "keyWord", required = false) String keyWord) {
        System.out.println(keyWord);
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            if (StrUtil.isNotBlank(token)) {
                user = userService.findByToken(token);
                System.out.println(user);
                if (user != null) {
                    session.setAttribute("user", user);
                }
            }
        }
        if (page == null || page <= 0) {
            page = 1;
        }
        if (StrUtil.isNotBlank(keyWord)) {
            model.addAttribute("keyWord", keyWord);
        }
        PageResult<List<ArticleEs>> allByEs = articleService.findAllByEs(page, keyWord);
        model.addAttribute("articles", allByEs);
        return "index";
    }
}
