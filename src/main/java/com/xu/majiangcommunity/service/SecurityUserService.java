package com.xu.majiangcommunity.service;

import java.util.ArrayList;
import java.util.List;

import com.xu.majiangcommunity.domain.SecurityUser;
import com.xu.majiangcommunity.domain.SecurityUserExample;
import com.xu.majiangcommunity.domain.UserView;
import com.xu.majiangcommunity.dto.PageResult;

public interface SecurityUserService {


    long countByExample(SecurityUserExample example);

    int deleteByExample(SecurityUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SecurityUser record);

    void insertSelective(SecurityUser record, String mail);

    List<SecurityUser> selectByExample(SecurityUserExample example);

    SecurityUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(SecurityUser record, SecurityUserExample example);

    int updateByExample(SecurityUser record, SecurityUserExample example);

    int updateByPrimaryKeySelective(SecurityUser record);

    int updateByPrimaryKey(SecurityUser record);

    int updateHeadByName(String username, String url);

    void zeroRound(String username);

    void plusNewRound(String articleId);

    PageResult<ArrayList<UserView>> getUserByIds(List<Integer> focusByUserId, Integer page);
}
