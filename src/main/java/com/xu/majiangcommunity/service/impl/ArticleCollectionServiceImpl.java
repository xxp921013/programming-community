package com.xu.majiangcommunity.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import com.xu.majiangcommunity.domain.ArticleCollection;
import com.xu.majiangcommunity.domain.ArticleCollectionExample;
import com.xu.majiangcommunity.dao.ArticleCollectionMapper;
import com.xu.majiangcommunity.service.ArticleCollectionService;

@Service
public class ArticleCollectionServiceImpl implements ArticleCollectionService {

    @Resource
    private ArticleCollectionMapper articleCollectionMapper;

    @Override
    public long countByExample(ArticleCollectionExample example) {
        return articleCollectionMapper.countByExample(example);
    }

    @Override
    @CacheEvict(value = "myCollection", key = "#username")
    public int deleteByExample(ArticleCollectionExample example, String username) {
        return articleCollectionMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return articleCollectionMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(ArticleCollection record) {
        return articleCollectionMapper.insert(record);
    }

    @Override
    @CacheEvict(value = "myCollection", key = "#record.username")
    public int insertSelective(ArticleCollection record) {
        return articleCollectionMapper.insertSelective(record);
    }

    @Override
    public List<ArticleCollection> selectByExample(ArticleCollectionExample example) {
        return articleCollectionMapper.selectByExample(example);
    }

    @Override
    public ArticleCollection selectByPrimaryKey(Integer id) {
        return articleCollectionMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(ArticleCollection record, ArticleCollectionExample example) {
        return articleCollectionMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExample(ArticleCollection record, ArticleCollectionExample example) {
        return articleCollectionMapper.updateByExample(record, example);
    }

    @Override
    public int updateByPrimaryKeySelective(ArticleCollection record) {
        return articleCollectionMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(ArticleCollection record) {
        return articleCollectionMapper.updateByPrimaryKey(record);
    }

    @Override
    @Cacheable(value = "myCollection", key = "#username")
    public List<Integer> getUserCollectionArticleIds(String username) {
        List<Integer> articleIdByUsername = articleCollectionMapper.findArticleIdByUsername(username);
        return articleIdByUsername;
    }


}
