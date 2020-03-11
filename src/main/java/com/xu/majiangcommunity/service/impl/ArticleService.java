package com.xu.majiangcommunity.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xu.majiangcommunity.constant.MqConstant;
import com.xu.majiangcommunity.constant.SortConstant;
import com.xu.majiangcommunity.dao.ArticleMapper;
import com.xu.majiangcommunity.dao.ArticleRepo;
import com.xu.majiangcommunity.domain.Article;
import com.xu.majiangcommunity.domain.ArticleEs;
import com.xu.majiangcommunity.domain.ArticleExample;
import com.xu.majiangcommunity.dto.ArticleDTO;
import com.xu.majiangcommunity.dto.ArticleDetailDTO;
import com.xu.majiangcommunity.dto.PageResult;
import com.xu.majiangcommunity.service.ArticleServiceIf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ArticleService implements ArticleServiceIf {
    private static final String HOTARTICLE = "HotArticle:";
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisTemplate<String, Serializable> redisCacheTemplate;
    @Autowired
    private ArticleRepo articleRepo;
    @Autowired
    private AmqpTemplate at;

    @Override
    public void addArticle(Article article) {
        articleMapper.insertSelective(article);
        Integer id = article.getId();
        if (id != null && id != 0) {
            at.convertAndSend(MqConstant.ARTICLE_EXCHANGE, MqConstant.ARTICLE_ROUTING_KEY_ADD, id);
        }
    }

    //@Cacheable(value = "findAll", key = "#page+'-'+#keyWord")
    @Override

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

    @Override

    public PageResult<List<Article>> findArticleByCreator(String id, Integer page) {
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

    @Override

    public ArticleDetailDTO getArticleDetail(String id) {
        ArticleDetailDTO articleDetailById = articleMapper.findArticleDetailById(id);
        return articleDetailById;
    }

    @Override


    public void viewArticle(String id) {
        articleMapper.viewArticle(id);
    }

    @Override

    public void commentArticle(String articleId) {
        articleMapper.commentArticle(articleId);
    }

    @Override

    public Article findById(Integer id) {
        Article article = articleMapper.findById(id);
        if (article == null) {
            log.error("[异常文章id],{}", id);
            throw new RuntimeException("查找不到此文章");
        }
        return article;
    }

    @Override

    public void updateArticle(Article article) {
        articleMapper.updateArticle(article);
    }

    @Override

    public void deleteArticle(Integer id) {
        articleMapper.deleteArticle(id);
    }

    @Override

    @Cacheable(value = "hotArticle", key = "#tags")
    public List<Article> getHotArticle(String tags) {
        PageHelper.startPage(1, 10);
        String replace = StrUtil.replace(tags, ",", "|");
        System.out.println(replace);
        List<Article> articles = articleMapper.getHotArticle(replace);
        return articles;
    }

    @Override

    public List<Article> getLastDayArticle(Long time) {
        List<Article> articles = articleMapper.getLastDayArticle(time);
        return articles;
    }

    @Override

    public Set<Serializable> getRecent() {
        BoundZSetOperations<String, Serializable> zSetOps = redisCacheTemplate.boundZSetOps(HOTARTICLE);
        Set<Serializable> range = zSetOps.reverseRange(0, 10);
        return range;
    }

    @Override

    public int countMyArticle(String accountId) {
        int i = articleMapper.countByCreator(accountId);
        return i;
    }

    @Override

    public PageResult<List<ArticleEs>> findAllByEs(Integer page, String keyWord, String sortType) {
        Sort sort = getSort(sortType);
        Pageable of = PageRequest.of(page - 1, 5, sort);
        ArrayList<ArticleEs> articleEs = new ArrayList<>();
        Page<ArticleEs> articleEsPage;
        if (StrUtil.isNotBlank(keyWord)) {
            String replace = keyWord.replace(" ", "");
            articleEsPage = articleRepo.findAllByDescriptionLikeOrTittleLike(replace, replace, of);
        } else {
            articleEsPage = articleRepo.findAll(of);
        }

        PageResult<List<ArticleEs>> listPageResult = new PageResult<>();
        listPageResult.setData(articleEsPage.getContent());
        listPageResult.setPageNum(page);
//        ArrayList<Integer> pages = null;
//        if (page == 1 && articleEsPage.getTotalPages() >= 5) {
//            pages = CollectionUtil.newArrayList(1, 2, 3, 4, 5);
//        } else if (articleEsPage.getTotalPages() <= 5) {
//            pages = new ArrayList<Integer>();
//            for (int i = 1; i <= articleEsPage.getTotalPages(); i++) {
//                pages.add(i);
//            }
//        } else {
//            pages = new ArrayList<Integer>();
//            for (int i = page - 1; i <= page + 3; i++) {
//                pages.add(i);
//            }
//        }
//        listPageResult.setPages(pages);
        listPageResult.setTotal(articleEsPage.getTotalPages());
        return listPageResult;
    }

    private Sort getSort(String sortType) {
        Sort sort = null;
        if ("1".equals(sortType)) {
            sort = new Sort(Sort.Direction.DESC, SortConstant.BY_ARTICLE_UPDATE);
        } else if ("2".equals(sortType)) {
            sort = new Sort(Sort.Direction.DESC, SortConstant.BY_ARTICLE_VIEWCOUNT);
        } else {
            sort = new Sort(Sort.Direction.DESC, SortConstant.BY_ARTICLE_COMMENTCOUNT);
        }
        return sort;
    }

    @Override

    public long countByExample(ArticleExample example) {
        return articleMapper.countByExample(example);
    }

    @Override

    public int deleteByExample(ArticleExample example) {
        return articleMapper.deleteByExample(example);
    }

    @Override

    public int deleteByPrimaryKey(Integer id) {
        return articleMapper.deleteByPrimaryKey(id);
    }

    @Override

    public int insert(Article record) {
        return articleMapper.insert(record);
    }

    @Override

    public int insertSelective(Article record) {
        return articleMapper.insertSelective(record);
    }

    @Override

    public List<Article> selectByExample(ArticleExample example) {
        return articleMapper.selectByExample(example);
    }

    @Override

    public Article selectByPrimaryKey(Integer id) {
        return articleMapper.selectByPrimaryKey(id);
    }

    @Override

    public int updateByExampleSelective(Article record, ArticleExample example) {
        return articleMapper.updateByExampleSelective(record, example);
    }

    @Override

    public int updateByExample(Article record, ArticleExample example) {
        return articleMapper.updateByExample(record, example);
    }

    @Override

    public int updateByPrimaryKeySelective(Article record) {
        return articleMapper.updateByPrimaryKeySelective(record);
    }

    @Override

    public int updateByPrimaryKey(Article record) {
        return articleMapper.updateByPrimaryKey(record);
    }

    @Override

    public ArticleDTO findOneById(Integer id) {
        ArticleDTO oneById = articleMapper.findOneById(id);
        return oneById;
    }

    @Override
    public Integer getViewCount(Integer id) {

        return articleMapper.findViewCountById(id);
    }
}

