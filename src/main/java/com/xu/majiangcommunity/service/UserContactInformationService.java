package com.xu.majiangcommunity.service;

import java.util.List;

import com.xu.majiangcommunity.domain.UserContactInformationExample;
import com.xu.majiangcommunity.domain.UserContactInformation;

public interface UserContactInformationService {


    long countByExample(UserContactInformationExample example);

    int deleteByExample(UserContactInformationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserContactInformation record);

    int insertSelective(UserContactInformation record);

    List<UserContactInformation> selectByExample(UserContactInformationExample example);

    UserContactInformation selectByPrimaryKey(Integer id);

    int updateByExampleSelective(UserContactInformation record, UserContactInformationExample example);

    int updateByExample(UserContactInformation record, UserContactInformationExample example);

    int updateByPrimaryKeySelective(UserContactInformation record);

    int updateByPrimaryKey(UserContactInformation record);

    UserContactInformation getOneByUsername(String username);
}
