package com.xu.majiangcommunity.dao;
import java.util.Collection;

import com.xu.majiangcommunity.domain.Tag;
import com.xu.majiangcommunity.domain.TagExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TagMapper {
    long countByExample(TagExample example);

    int deleteByExample(TagExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Tag record);

    int insertSelective(Tag record);

    List<Tag> selectByExample(TagExample example);

    Tag selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Tag record, @Param("example") TagExample example);

    int updateByExample(@Param("record") Tag record, @Param("example") TagExample example);

    int updateByPrimaryKeySelective(Tag record);

    int updateByPrimaryKey(Tag record);

    List<Tag> findAllByNameNotNull();


    List<Tag> findAllByNameIn(@Param("nameCollection")Collection<String> nameCollection);



}
