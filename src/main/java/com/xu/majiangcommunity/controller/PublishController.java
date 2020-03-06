package com.xu.majiangcommunity.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.xu.majiangcommunity.domain.Article;
import com.xu.majiangcommunity.domain.Rounds;
import com.xu.majiangcommunity.domain.User;
import com.xu.majiangcommunity.dto.PageResult;
import com.xu.majiangcommunity.service.impl.ArticleService;
import com.xu.majiangcommunity.service.impl.RoundService;
import com.xu.majiangcommunity.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PublishController {
    @Autowired
    private UserService userService;
    @Autowired
    private ArticleService articleService;

    @Autowired
    private RoundService roundService;

    @GetMapping("/publish")
    public String getPublish(Model model) {
        List<String> tags = new ArrayList<>();
        tags.add("java");
        tags.add("spring");
        tags.add("springBoot");
        tags.add("springMvc");
        tags.add("jvm");
        tags.add("html");
        tags.add("css");
        tags.add("redis");
        tags.add("mq");
        tags.add("mysql");
        tags.add("juc");
        tags.add("es");
        model.addAttribute("allowedtags", tags);
        return "publish";
    }

    @PostMapping("/addArticle")
    public String addArticle(Article article, @CookieValue(value = "token", required = false) String token) {
        if (StrUtil.isBlank(token)) {
            return "redirect:/";
        }
        User byToken = userService.findByToken(token);
        article.setGmtCreate(DateUtil.current(false));
        article.setCommentCount(0);
        article.setGmtModified(DateUtil.current(false));
        article.setViewCount(0);
        article.setLikeCount(0);
        article.setCreator(byToken.getAccountId());
        articleService.addArticle(article);
        return "redirect:/";
    }

    @GetMapping("/userArticle")
    public String getUserArticle(@RequestParam(value = "id", required = false) String id, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page, HttpServletRequest req, Model model) {
        if (id == null) {
            return "redirect:/";
        }
        if (page == null || page <= 0) {
            page = 1;
        }
        PageResult<List<Article>> articles = articleService.findArticleByCreator(id, page);
        model.addAttribute("myArticle", articles);
        model.addAttribute("selection", "我的文章");
        return "myArticle";
    }

    @GetMapping("/myRound")
    public String getMyNewRound(@RequestParam("id") String id, @RequestParam(value = "page", required = false) Integer page, Model model) {
        if (page == null || page <= 0) {
            page = 1;
        }
        PageResult<List<Rounds>> rounds = roundService.findRoundByUid(id, page);
        model.addAttribute("rounds", rounds);
        model.addAttribute("selection", "我的回复");
        userService.zeroRound();
        return "myRound";
    }
}
