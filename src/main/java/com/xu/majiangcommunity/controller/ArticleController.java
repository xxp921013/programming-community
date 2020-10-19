package com.xu.majiangcommunity.controller;

import cn.hutool.core.bean.BeanUtil;
import com.xu.majiangcommunity.dao.ArticleMapper;
import com.xu.majiangcommunity.dao.ArticleRepo;
import com.xu.majiangcommunity.domain.Article;
import com.xu.majiangcommunity.domain.ArticleEs;
import com.xu.majiangcommunity.domain.SecurityUser;
import com.xu.majiangcommunity.domain.Tag;
import com.xu.majiangcommunity.dto.ArticleDTO;
import com.xu.majiangcommunity.dto.ArticleDetailDTO;
import com.xu.majiangcommunity.dto.BaseResponseBody;
import com.xu.majiangcommunity.interceptor.UserInterceptor;
import com.xu.majiangcommunity.service.ArticleCollectionService;
import com.xu.majiangcommunity.service.TagService;
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
import java.util.stream.Collectors;

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
    @Autowired
    private ArticleCollectionService articleCollectionService;
    @Autowired
    private TagService tagService;

    @GetMapping("/articleDetail")
    public String getArticleDetail(@RequestParam("id") String id, Model model) {
        ArticleDetailDTO articleDetailDTO = articleService.getArticleDetail(id);
        if (articleDetailDTO != null) {
            articleService.viewArticle(id);
        }
        Integer idi = Integer.valueOf(id);
        Integer view = articleService.getViewCount(idi);
        ArticleEs articleEs = new ArticleEs();
        BeanUtil.copyProperties(articleDetailDTO, articleEs);
        articleEs.setId(idi);
        articleEs.setViewCount(view);
        articleEs.setUserImg(articleDetailDTO.getUser().getImage());
        articleEs.setUserId(articleDetailDTO.getUser().getId());
        articleEs.setUsername(articleDetailDTO.getUser().getUsername());
        articleRepo.save(articleEs);
        model.addAttribute("articleDetail", articleDetailDTO);
        SecurityUser user = UserInterceptor.getUser();
        if (user != null) {
            List<Integer> articleIds = articleCollectionService.getUserCollectionArticleIds(user.getUsername());
            System.out.println(articleIds);
            if (articleIds.contains(Integer.valueOf(id))) {
                model.addAttribute("collectionType", "2");
            } else {
                model.addAttribute("collectionType", "1");
            }
        } else {
            model.addAttribute("collectionType", "1");
        }

        return "articleDetail";
    }

    @GetMapping("/modifiedArticle")
    public String modifiedArticle(@RequestParam("id") Integer id, Model model) {
        Article byId = articleService.findById(id);
        List<Tag> tagList = tagService.getList();
        List<String> strings = tagList.parallelStream().map(Tag::getName).collect(Collectors.toList());
        model.addAttribute("allowedtags", strings);
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
        articleRepo.deleteById(id);
        return "redirect:/userArticle";
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
            articleEs.setUsername(articleDTO.getUser().getUsername());
            articleEs.setUserId(articleDTO.getUser().getId());
            articleEss.add(articleEs);
        }
        articleRepo.saveAll(articleEss);
        return "成功";
    }


}
