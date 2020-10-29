package com.xu.majiangcommunity.service.impl;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import com.xu.majiangcommunity.domain.HotArticleExample;
import com.xu.majiangcommunity.domain.HotArticle;
import com.xu.majiangcommunity.dao.HotArticleMapper;
import com.xu.majiangcommunity.service.HotArticleService;

@Service
public class HotArticleServiceImpl implements HotArticleService {

    @Resource
    private HotArticleMapper hotArticleMapper;

    @Override
    public long countByExample(HotArticleExample example) {
        return hotArticleMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(HotArticleExample example) {
        return hotArticleMapper.deleteByExample(example);
    }

    @Override
    public int insert(HotArticle record) {
        return hotArticleMapper.insert(record);
    }

    @Override
    public int insertSelective(HotArticle record) {
        return hotArticleMapper.insertSelective(record);
    }

    @Override
    public List<HotArticle> selectByExample(HotArticleExample example) {
        return hotArticleMapper.selectByExample(example);
    }

    @Override
    public int updateByExampleSelective(HotArticle record, HotArticleExample example) {
        return hotArticleMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExample(HotArticle record, HotArticleExample example) {
        return hotArticleMapper.updateByExample(record, example);
    }

    @Override
    public List<HotArticle> selectByArticleIdIn(List<Integer> ids) {
        HotArticleExample hotArticleExample = new HotArticleExample();
        hotArticleExample.or().andArticleIdIn(ids);
        List<HotArticle> hotArticles = hotArticleMapper.selectByExample(hotArticleExample);
        return hotArticles;
    }

}
