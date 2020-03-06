package com.xu.majiangcommunity.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.xu.majiangcommunity.dao.UserMapper;
import com.xu.majiangcommunity.domain.User;
import com.xu.majiangcommunity.domain.UserExample;
import com.xu.majiangcommunity.service.UserServiceIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserService implements UserServiceIf {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void addUser(User mainUser) {
        userMapper.addUser(mainUser);
    }

    @Override

    public User findByToken(String token) {
        User byToken = userMapper.findByToken(token);
        return byToken;
    }

    @Override

    public User findById(String id) {
        User byId = userMapper.findById(id);
        return byId;
    }

    @Override

    public List<User> findByAccountId(Long id) {
        List<User> users = userMapper.findByAccountId(id);
        return users;
    }

    @Override

    public void zeroRound() {
        userMapper.zeroRound();
    }

    @Override

    public void plusNewRound(String articleId) {
        userMapper.plusNewRound(articleId);
    }

    @Override

    public long countByExample(UserExample example) {
        return userMapper.countByExample(example);
    }

    @Override

    public int deleteByExample(UserExample example) {
        return userMapper.deleteByExample(example);
    }

    @Override

    public int deleteByPrimaryKey(Integer id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    @Override

    public int insert(User record) {
        return userMapper.insert(record);
    }

    @Override

    public int insertSelective(User record) {
        record.setImage("http://localhost:8080/images/timg.jpg");
        record.setGmtModified(DateUtil.current(false));
        record.setGmtCreate(DateUtil.current(false));
        return userMapper.insertSelective(record);
    }

    @Override

    public List<User> selectByExample(UserExample example) {
        return userMapper.selectByExample(example);
    }

    @Override

    public User selectByPrimaryKey(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override

    public int updateByExampleSelective(User record, UserExample example) {
        return userMapper.updateByExampleSelective(record, example);
    }

    @Override

    public int updateByExample(User record, UserExample example) {
        return userMapper.updateByExample(record, example);
    }

    @Override

    public int updateByPrimaryKeySelective(User record) {
        return userMapper.updateByPrimaryKeySelective(record);
    }

    @Override

    public int updateByPrimaryKey(User record) {
        return userMapper.updateByPrimaryKey(record);
    }

    @Override

    public User getById(Integer id) {
        return userMapper.getById(id);
    }

    @Override

    public int updateHeadByName(String name, String url) {
        List<User> byName = userMapper.getByName(name);
        if (CollectionUtil.isEmpty(byName)) {
            return 0;
        }
        int i = userMapper.updateImageByName(url, name);
        return i;
    }
}

