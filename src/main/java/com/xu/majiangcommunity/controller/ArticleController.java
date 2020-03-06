package com.xu.majiangcommunity.controller;

import cn.hutool.core.bean.BeanUtil;
import com.xu.majiangcommunity.dao.ArticleMapper;
import com.xu.majiangcommunity.dao.ArticleRepo;
import com.xu.majiangcommunity.domain.Article;
import com.xu.majiangcommunity.domain.ArticleEs;
import com.xu.majiangcommunity.dto.ArticleDTO;
import com.xu.majiangcommunity.dto.ArticleDetailDTO;
import com.xu.majiangcommunity.dto.BaseResponseBody;
import com.xu.majiangcommunity.service.impl.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
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
    @Autowired
    private RedisTemplate<String, Serializable> redisCacheTemplate;
    private static final String HOT_TAG = "HotTag:";
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleRepo articleRepo;

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
    public BaseResponseBody<List<Article>> getHotArticle(@RequestParam("tags") String tags) {
        List<Article> articles = articleService.getHotArticle(tags);
        return new BaseResponseBody<List<Article>>(200, "查找热门文章成功", articles);
    }

    @ResponseBody
    @GetMapping("/recentHot")
    public BaseResponseBody<Set<Serializable>> getRecent() {
        Set<Serializable> recent = articleService.getRecent();
        return new BaseResponseBody<>(200, "查找成功", recent);
    }

    @ResponseBody
    @GetMapping("/hotTags")
    public BaseResponseBody<Set> hotTags() {
        BoundZSetOperations<String, Serializable> zSetOps = redisCacheTemplate.boundZSetOps(HOT_TAG);
        Set<Serializable> serializables = zSetOps.reverseRange(0, 5);
        return new BaseResponseBody<>(200, "查找热门标签成功", serializables);
    }

    @ResponseBody
    @GetMapping("/build")
    public String build() {
        List<ArticleDTO> dtObyID = articleMapper.findDTObyID();
        ArticleEs articleEs = null;
        ArrayList<ArticleEs> articleEss = new ArrayList<>();
        for (ArticleDTO articleDTO : dtObyID) {
            articleEs = new ArticleEs();
            BeanUtil.copyProperties(articleDTO, articleEs);
            articleEs.setUserImg(articleDTO.getUser().getImage());
            articleEss.add(articleEs);
        }
        articleRepo.saveAll(articleEss);
        return "成功";
    }
}
