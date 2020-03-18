package com.xu.majiangcommunity.service.impl;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.xu.majiangcommunity.dao.UserContactInformationMapper;

import java.util.List;

import com.xu.majiangcommunity.domain.UserContactInformationExample;
import com.xu.majiangcommunity.domain.UserContactInformation;
import com.xu.majiangcommunity.service.UserContactInformationService;

@Service
public class UserContactInformationServiceImpl implements UserContactInformationService {

    @Resource
    private UserContactInformationMapper userContactInformationMapper;

    @Override
    public long countByExample(UserContactInformationExample example) {
        return userContactInformationMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(UserContactInformationExample example) {
        return userContactInformationMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return userContactInformationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UserContactInformation record) {
        return userContactInformationMapper.insert(record);
    }

    @Override
    public int insertSelective(UserContactInformation record) {
        return userContactInformationMapper.insertSelective(record);
    }

    @Override
    public List<UserContactInformation> selectByExample(UserContactInformationExample example) {
        return userContactInformationMapper.selectByExample(example);
    }

    @Override
    public UserContactInformation selectByPrimaryKey(Integer id) {
        return userContactInformationMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(UserContactInformation record, UserContactInformationExample example) {
        return userContactInformationMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExample(UserContactInformation record, UserContactInformationExample example) {
        return userContactInformationMapper.updateByExample(record, example);
    }

    @Override
    public int updateByPrimaryKeySelective(UserContactInformation record) {
        return userContactInformationMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UserContactInformation record) {
        return userContactInformationMapper.updateByPrimaryKey(record);
    }

    @Override
    public UserContactInformation getOneByUsername(String username) {
        return userContactInformationMapper.getOneByUsername(username);
    }

}
