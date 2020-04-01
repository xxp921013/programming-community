package com.xu.majiangcommunity.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.xu.majiangcommunity.UserException;
import com.xu.majiangcommunity.domain.UserContactInformation;
import com.xu.majiangcommunity.domain.UserView;
import com.xu.majiangcommunity.dto.PageResult;
import com.xu.majiangcommunity.enums.ExcetionEnmu;
import com.xu.majiangcommunity.service.UserContactInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import com.xu.majiangcommunity.dao.SecurityUserMapper;
import com.xu.majiangcommunity.domain.SecurityUser;
import com.xu.majiangcommunity.domain.SecurityUserExample;
import com.xu.majiangcommunity.service.SecurityUserService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SecurityUserServiceImpl implements SecurityUserService, UserDetailsService {
    @Autowired
    private UserContactInformationService ucis;
    @Resource
    private SecurityUserMapper securityUserMapper;

    @Override
    public long countByExample(SecurityUserExample example) {
        return securityUserMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(SecurityUserExample example) {
        return securityUserMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return securityUserMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(SecurityUser record) {
        return securityUserMapper.insert(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void insertSelective(SecurityUser record, String mail) {
        record.setImage("http://localhost:8080/images/timg.jpg");
        record.setGmtModified(DateUtil.current(false));
        record.setGmtCreate(DateUtil.current(false));
        int i = securityUserMapper.insertSelective(record);
        if (i != 1) {
            throw new UserException(ExcetionEnmu.REGISTRY_FAILED);
        }
        UserContactInformation userContactInformation = new UserContactInformation();
        userContactInformation.setUsername(record.getUsername());
        userContactInformation.setMail(mail);
        int y = ucis.insertSelective(userContactInformation);
        if (y != 1) {
            throw new UserException(ExcetionEnmu.REGISTRY_FAILED);
        }
    }

    @Override
    public List<SecurityUser> selectByExample(SecurityUserExample example) {
        return securityUserMapper.selectByExample(example);
    }

    @Override
    public SecurityUser selectByPrimaryKey(Integer id) {
        return securityUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(SecurityUser record, SecurityUserExample example) {
        return securityUserMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExample(SecurityUser record, SecurityUserExample example) {
        return securityUserMapper.updateByExample(record, example);
    }

    @Override
    public int updateByPrimaryKeySelective(SecurityUser record) {
        return securityUserMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SecurityUser record) {
        return securityUserMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateHeadByName(String username, String url) {
        int i = securityUserMapper.updateImageByUsername(url, username);
        return i;
    }

    @Override
    public void zeroRound(String username) {
        securityUserMapper.zeroRound(username);
    }

    @Override
    public void plusNewRound(String articleId) {
        securityUserMapper.plusNewRound(articleId);
    }

    @Override
    public PageResult<ArrayList<UserView>> getUserByIds(List<Integer> focusByUserId, Integer page) {
        PageHelper.startPage(page, 10);
        List<UserView> userViews = securityUserMapper.selectUsernameAndIdAndImagebyidIn(focusByUserId);
        PageInfo<UserView> userViewPageInfo = new PageInfo<>(userViews);
        PageResult<ArrayList<UserView>> arrayListPageResult = new PageResult<>();
        arrayListPageResult.setCode(200);
        arrayListPageResult.setMessage("查找成功");
        arrayListPageResult.setPageNum(page);
        arrayListPageResult.setTotal((int) userViewPageInfo.getTotal());
        arrayListPageResult.setData(new ArrayList<>(userViews));
        return arrayListPageResult;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        SecurityUser securityUser = new SecurityUser();
        securityUser.setUsername(s);
        SecurityUserExample securityUserExample = new SecurityUserExample();
        securityUserExample.or().andUsernameEqualTo(s);
        List<SecurityUser> securityUsers = securityUserMapper.selectByExample(securityUserExample);
        if (CollectionUtil.isEmpty(securityUsers)) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        SecurityUser user = securityUsers.get(0);
        return user;
    }
}
