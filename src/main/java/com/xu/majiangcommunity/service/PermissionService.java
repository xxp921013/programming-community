package com.xu.majiangcommunity.service;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.xu.majiangcommunity.domain.PermissionExample;
import com.xu.majiangcommunity.domain.Permission;
import com.xu.majiangcommunity.dao.PermissionMapper;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionService{

    @Resource
    private PermissionMapper permissionMapper;

    
    public long countByExample(PermissionExample example) {
        return permissionMapper.countByExample(example);
    }

    
    public int deleteByExample(PermissionExample example) {
        return permissionMapper.deleteByExample(example);
    }

    
    public int deleteByPrimaryKey(Integer id) {
        return permissionMapper.deleteByPrimaryKey(id);
    }

    
    public int insert(Permission record) {
        return permissionMapper.insert(record);
    }

    
    public int insertSelective(Permission record) {
        return permissionMapper.insertSelective(record);
    }

    
    public List<Permission> selectByExample(PermissionExample example) {
        return permissionMapper.selectByExample(example);
    }

    
    public Permission selectByPrimaryKey(Integer id) {
        return permissionMapper.selectByPrimaryKey(id);
    }

    
    public int updateByExampleSelective(Permission record,PermissionExample example) {
        return permissionMapper.updateByExampleSelective(record,example);
    }

    
    public int updateByExample(Permission record,PermissionExample example) {
        return permissionMapper.updateByExample(record,example);
    }

    
    public int updateByPrimaryKeySelective(Permission record) {
        return permissionMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(Permission record) {
        return permissionMapper.updateByPrimaryKey(record);
    }

}
