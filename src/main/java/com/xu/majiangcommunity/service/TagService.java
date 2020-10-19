package com.xu.majiangcommunity.service;

import com.xu.majiangcommunity.domain.Tag;
import java.util.List;
import com.xu.majiangcommunity.domain.TagExample;
public interface TagService{


    long countByExample(TagExample example);

    int deleteByExample(TagExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Tag record);

    int insertSelective(Tag record);

    List<Tag> selectByExample(TagExample example);

    Tag selectByPrimaryKey(Integer id);

    int updateByExampleSelective(Tag record,TagExample example);

    int updateByExample(Tag record,TagExample example);

    int updateByPrimaryKeySelective(Tag record);

    int updateByPrimaryKey(Tag record);

    List<Tag> getList();

}
