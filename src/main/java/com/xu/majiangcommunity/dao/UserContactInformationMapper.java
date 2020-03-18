package com.xu.majiangcommunity.dao;

import com.xu.majiangcommunity.domain.UserContactInformation;
import com.xu.majiangcommunity.domain.UserContactInformationExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface UserContactInformationMapper {
    long countByExample(UserContactInformationExample example);

    int deleteByExample(UserContactInformationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserContactInformation record);

    int insertSelective(UserContactInformation record);

    List<UserContactInformation> selectByExample(UserContactInformationExample example);

    UserContactInformation selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserContactInformation record, @Param("example") UserContactInformationExample example);

    int updateByExample(@Param("record") UserContactInformation record, @Param("example") UserContactInformationExample example);

    int updateByPrimaryKeySelective(UserContactInformation record);

    int updateByPrimaryKey(UserContactInformation record);

    UserContactInformation getOneByUsername(@Param("username") String username);


}
