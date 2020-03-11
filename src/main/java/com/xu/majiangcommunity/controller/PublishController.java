package com.xu.majiangcommunity.controller;

import cn.hutool.core.date.DateUtil;
import com.xu.majiangcommunity.UserException;
import com.xu.majiangcommunity.domain.Article;
import com.xu.majiangcommunity.domain.Rounds;
import com.xu.majiangcommunity.domain.SecurityUser;
import com.xu.majiangcommunity.dto.PageResult;
import com.xu.majiangcommunity.enums.ExcetionEnmu;
import com.xu.majiangcommunity.interceptor.UserInterceptor;
import com.xu.majiangcommunity.service.SecurityUserService;
import com.xu.majiangcommunity.service.impl.ArticleService;
import com.xu.majiangcommunity.service.impl.RoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PublishController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RoundService roundService;
    @Autowired
    private SecurityUserService securityUserService;

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
    public String addArticle(Article article) {

        SecurityUser user = UserInterceptor.getUser();
        article.setGmtCreate(DateUtil.current(false));
        article.setCommentCount(0);
        article.setGmtModified(DateUtil.current(false));
        article.setViewCount(0);
        article.setLikeCount(0);
        article.setCreator(user.getUsername());
        articleService.addArticle(article);
        return "redirect:/";
    }

    @GetMapping("/userArticle")
    public String getUserArticle(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page, HttpServletRequest req, Model model) {
        if (page == null || page <= 0) {
            page = 1;
        }
        SecurityUser user = UserInterceptor.getUser();
        if (user == null) {
            throw new UserException(ExcetionEnmu.TEST_THROW);
        }
        PageResult<List<Article>> articles = articleService.findArticleByCreator(user.getUsername(), page);
        model.addAttribute("myArticle", articles);
        model.addAttribute("selection", "我的文章");
        return "myArticle";
    }

    @GetMapping("/myRound")
    public String getMyNewRound(@RequestParam(value = "page", required = false) Integer page, Model model) {
        if (page == null || page <= 0) {
            page = 1;
        }
        SecurityUser user = UserInterceptor.getUser();
        if (user == null) {
            throw new UserException(ExcetionEnmu.TEST_THROW);
        }
        PageResult<List<Rounds>> rounds = roundService.findRoundByUid(user.getUsername(), page);
        model.addAttribute("rounds", rounds);
        model.addAttribute("selection", "我的回复");
        securityUserService.zeroRound(user.getUsername());
        return "myRound";
    }
}
