package com.xu.majiangcommunity.controller;

import cn.hutool.core.util.StrUtil;
import com.xu.majiangcommunity.domain.ArticleEs;
import com.xu.majiangcommunity.domain.User;
import com.xu.majiangcommunity.dto.PageResult;
import com.xu.majiangcommunity.service.impl.ArticleService;
import com.xu.majiangcommunity.service.impl.UserService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

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
                           @RequestParam(value = "keyWord", required = false) String keyWord,
                           @RequestParam(value = "sortType", required = false, defaultValue = "1") String sortType) {
        System.out.println(sortType);
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
        if ("null".equals(keyWord)) {
            keyWord = null;
        }
        if (StrUtil.isNotBlank(keyWord)) {
            model.addAttribute("keyWord", keyWord);
        }
        PageResult<List<ArticleEs>> allByEs = articleService.findAllByEs(page, keyWord, sortType);
        model.addAttribute("articles", allByEs);
        model.addAttribute("sortType", sortType);
        return "index";
    }

    @GetMapping("/page")
    public String goPage() {
        return "paging";
    }
}
