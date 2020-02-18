package com.xu.majiangcommunity.controller;

import cn.hutool.core.util.StrUtil;
import com.xu.majiangcommunity.dao.ArticleMapper;
import com.xu.majiangcommunity.domain.Article;
import com.xu.majiangcommunity.domain.User;
import com.xu.majiangcommunity.dto.ArticleDTO;
import com.xu.majiangcommunity.dto.PageResult;
import com.xu.majiangcommunity.service.ArticleService;
import com.xu.majiangcommunity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Struct;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @GetMapping("/")
    public String getIndex(@CookieValue(value = "token", required = false) String token,
                           HttpServletRequest req, Model model,
                           @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                           @RequestParam(value = "keyWord", required = false) String keyWord) {
        System.out.println(keyWord);
        if (StrUtil.isNotBlank(token)) {
            User user = userService.findByToken(token);
            System.out.println(user);
            if (user != null) {
                req.getSession().setAttribute("user", user);
            }
        }
        if (page == null || page <= 0) {
            page = 1;
        }
        if (StrUtil.isNotBlank(keyWord)) {
            model.addAttribute("keyWord", keyWord);
        }
        PageResult<List<ArticleDTO>> all = articleService.findAll(page, keyWord);
        model.addAttribute("articles", all);
        return "index";
    }
}
