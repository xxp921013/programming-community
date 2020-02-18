package com.xu.majiangcommunity.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xu.majiangcommunity.dao.ArticleMapper;
import com.xu.majiangcommunity.domain.Article;
import com.xu.majiangcommunity.dto.ArticleDTO;
import com.xu.majiangcommunity.dto.ArticleDetailDTO;
import com.xu.majiangcommunity.dto.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ArticleService {
    private static final String HOTARTICLE = "HotArticle:";
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String, Serializable> redisCacheTemplate;


    public void addArticle(Article article) {
        articleMapper.addArticle(article);
    }

    public PageResult<List<ArticleDTO>> findAll(Integer page, String keyWord) {
        PageHelper.startPage(page, 5);
        List<ArticleDTO> dtObyID = null;
        if (StrUtil.isNotBlank(keyWord)) {
            keyWord = StrUtil.replace(keyWord, " ", "|");
            dtObyID = articleMapper.findDTObyIDWithKeyWord(keyWord);
        } else {
            dtObyID = articleMapper.findDTObyID();
        }

        PageInfo<ArticleDTO> articleDTOPageInfo = new PageInfo<>(dtObyID);
        PageResult<List<ArticleDTO>> listPageResult = new PageResult<>();
        listPageResult.setData(dtObyID);
        listPageResult.setPageNum(articleDTOPageInfo.getPageNum());
        ArrayList<Integer> pages = null;
        if (page == 1 && articleDTOPageInfo.getPages() >= 5) {
            pages = CollectionUtil.newArrayList(1, 2, 3, 4, 5);
        } else if (articleDTOPageInfo.getPages() <= 5) {
            pages = new ArrayList<Integer>();
            for (int i = 1; i <= articleDTOPageInfo.getPages(); i++) {
                pages.add(i);
            }
        } else {
            pages = new ArrayList<Integer>();
            for (int i = page - 1; i <= page + 3; i++) {
                pages.add(i);
            }
        }
        listPageResult.setPages(pages);
        // listPageResult.setPages();
        return listPageResult;
    }

    public PageResult<List<Article>> findArticleByCreator(Integer id, Integer page) {
        PageHelper.startPage(page, 5);
        List<Article> articles = articleMapper.findArticleByCreator(id);
        PageInfo<Article> articlePageInfo = new PageInfo<>(articles);
        PageResult<List<Article>> listPageResult = new PageResult<>();
        listPageResult.setData(articles);
        ArrayList<Integer> pages = null;
        if (page == 1 && articlePageInfo.getPages() >= 5) {
            pages = CollectionUtil.newArrayList(1, 2, 3, 4, 5);
        } else if (articlePageInfo.getPages() <= 5) {
            pages = new ArrayList<Integer>();
            for (int i = 1; i <= articlePageInfo.getPages(); i++) {
                pages.add(i);
            }
        } else {
            pages = new ArrayList<Integer>();
            for (int i = page - 1; i <= page + 3; i++) {
                pages.add(i);
            }
        }
        listPageResult.setPages(pages);
        listPageResult.setPageNum(articlePageInfo.getPageNum());
        listPageResult.setTotal((int) articlePageInfo.getTotal());
        return listPageResult;
    }

    public ArticleDetailDTO getArticleDetail(Integer id) {
        ArticleDetailDTO articleDetailById = articleMapper.findArticleDetailById(id);
        return articleDetailById;
    }

    public void viewArticle(Integer id) {
        articleMapper.viewArticle(id);
    }

    public void commentArticle(Integer articleId) {
        articleMapper.commentArticle(articleId);
    }

    public Article findById(Integer id) {
        Article article = articleMapper.findById(id);
        if (article == null) {
            throw new RuntimeException("查找不到此文章");
        }
        return article;
    }

    public void updateArticle(Article article) {
        articleMapper.updateArticle(article);
    }

    public void deleteArticle(Integer id) {
        articleMapper.deleteArticle(id);
    }

    public List<Article> getHotArticle(String tags) {
        PageHelper.startPage(1, 10);
        String replace = StrUtil.replace(tags, ",", "|");
        System.out.println(replace);
        List<Article> articles = articleMapper.getHotArticle(replace);
        return articles;
    }

    public List<Article> getLastDayArticle(Long time) {
        List<Article> articles = articleMapper.getLastDayArticle(time);
        return articles;
    }


    public Set<Serializable> getRecent() {
        BoundZSetOperations<String, Serializable> zSetOps = redisCacheTemplate.boundZSetOps(HOTARTICLE);
        Set<Serializable> range = zSetOps.reverseRange(0, 10);
        return range;
    }
}
