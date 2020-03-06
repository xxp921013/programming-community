package com.xu.majiangcommunity.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.xu.majiangcommunity.dao.WebLogMapper;
import java.util.List;
import com.xu.majiangcommunity.domain.WebLog;
import com.xu.majiangcommunity.domain.WebLogExample;

@Service
public class WebLogService {

    @Resource
    private WebLogMapper webLogMapper;


    public long countByExample(WebLogExample example) {
        return webLogMapper.countByExample(example);
    }


    public int deleteByExample(WebLogExample example) {
        return webLogMapper.deleteByExample(example);
    }


    public int deleteByPrimaryKey(Integer id) {
        return webLogMapper.deleteByPrimaryKey(id);
    }


    public int insert(WebLog record) {
        return webLogMapper.insert(record);
    }


    public int insertSelective(WebLog record) {
        return webLogMapper.insertSelective(record);
    }


    public List<WebLog> selectByExample(WebLogExample example) {
        return webLogMapper.selectByExample(example);
    }


    public WebLog selectByPrimaryKey(Integer id) {
        return webLogMapper.selectByPrimaryKey(id);
    }


    public int updateByExampleSelective(WebLog record, WebLogExample example) {
        return webLogMapper.updateByExampleSelective(record, example);
    }


    public int updateByExample(WebLog record, WebLogExample example) {
        return webLogMapper.updateByExample(record, example);
    }


    public int updateByPrimaryKeySelective(WebLog record) {
        return webLogMapper.updateByPrimaryKeySelective(record);
    }


    public int updateByPrimaryKey(WebLog record) {
        return webLogMapper.updateByPrimaryKey(record);
    }

}

