package com.xu.majiangcommunity.service.impl;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.xu.majiangcommunity.domain.Tag;

import java.util.List;

import com.xu.majiangcommunity.dao.TagMapper;
import com.xu.majiangcommunity.domain.TagExample;
import com.xu.majiangcommunity.service.TagService;

@Service
public class TagServiceImpl implements TagService {

    @Resource
    private TagMapper tagMapper;

    @Override
    public long countByExample(TagExample example) {
        return tagMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(TagExample example) {
        return tagMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return tagMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Tag record) {
        return tagMapper.insert(record);
    }

    @Override
    public int insertSelective(Tag record) {
        return tagMapper.insertSelective(record);
    }

    @Override
    public List<Tag> selectByExample(TagExample example) {
        return tagMapper.selectByExample(example);
    }

    @Override
    public Tag selectByPrimaryKey(Integer id) {
        return tagMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(Tag record, TagExample example) {
        return tagMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExample(Tag record, TagExample example) {
        return tagMapper.updateByExample(record, example);
    }

    @Override
    public int updateByPrimaryKeySelective(Tag record) {
        return tagMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Tag record) {
        return tagMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<Tag> getList() {
        return tagMapper.findAllByNameNotNull();
    }

}
