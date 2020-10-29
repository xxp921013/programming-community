package com.xu.majiangcommunity.controller;

import cn.hutool.core.date.DateUtil;
import com.xu.majiangcommunity.UserException;
import com.xu.majiangcommunity.constant.RedisPrefix;
import com.xu.majiangcommunity.domain.Article;
import com.xu.majiangcommunity.domain.Rounds;
import com.xu.majiangcommunity.domain.SecurityUser;
import com.xu.majiangcommunity.domain.Tag;
import com.xu.majiangcommunity.dto.PageResult;
import com.xu.majiangcommunity.enums.ExcetionEnmu;
import com.xu.majiangcommunity.interceptor.UserInterceptor;
import com.xu.majiangcommunity.service.SecurityUserService;
import com.xu.majiangcommunity.service.TagService;
import com.xu.majiangcommunity.service.impl.ArticleService;
import com.xu.majiangcommunity.service.impl.RoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PublishController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RoundService roundService;
    @Autowired
    private SecurityUserService securityUserService;
    @Autowired
    private TagService tagService;
    @Autowired
    private StringRedisTemplate srt;

    @GetMapping("/publish")
    public String getPublish(Model model) {
        List<Tag> tagList = tagService.getList();
        List<String> strings = tagList.parallelStream().map(Tag::getName).collect(Collectors.toList());
        model.addAttribute("allowedtags", strings);
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
        srt.opsForValue().increment(RedisPrefix.TODAY_ARTICLE_ADD, 1);
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
