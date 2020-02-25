package com.xu.majiangcommunity.controller;

import com.xu.majiangcommunity.domain.Article;
import com.xu.majiangcommunity.domain.HotArticle;
import com.xu.majiangcommunity.dto.ArticleDetailDTO;
import com.xu.majiangcommunity.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping("/articleDetail")
    public String getArticleDetail(@RequestParam("id") String id, Model model) {
        ArticleDetailDTO articleDetailDTO = articleService.getArticleDetail(id);
        if (articleDetailDTO != null) {
            articleService.viewArticle(id);
        }
        model.addAttribute("articleDetail", articleDetailDTO);
        return "articleDetail";
    }

    @GetMapping("/modifiedArticle")
    public String modifiedArticle(@RequestParam("id") Integer id, Model model) {
        Article byId = articleService.findById(id);
        List<String> tags = new ArrayList<>();
        tags.add("java");
        tags.add("spring");
        tags.add("spring boot");
        tags.add("spring mvc");
        tags.add("jvm");
        tags.add("html");
        tags.add("css");
        tags.add("redis");
        tags.add("mq");
        tags.add("mysql");
        tags.add("juc");
        tags.add("es");
        model.addAttribute("allowedtags", tags);
        model.addAttribute("article", byId);
        return "updateArticle";
    }

    @PostMapping("/updateArticle")
    public String updateArticle(Article article) {
        article.setGmtModified(System.currentTimeMillis());
        articleService.updateArticle(article);
        return "redirect:/";
    }

    @GetMapping("/deleteArticle")
    public String deleteArticle(@RequestParam("id") Integer id) {
        articleService.deleteArticle(id);
        return "redirect:/";
    }

    @GetMapping("/hotArticle")
    @ResponseBody
    public List<Article> getHotArticle(@RequestParam("tags") String tags) {
        List<Article> articles = articleService.getHotArticle(tags);
        return articles;
    }

    @ResponseBody
    @GetMapping("/recentHot")
    public Set<Serializable> getRecent() {
        Set<Serializable> recent = articleService.getRecent();
        return recent;
    }
}
