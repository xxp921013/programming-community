package com.xu.majiangcommunity.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xu.majiangcommunity.dao.RoundMapper;
import com.xu.majiangcommunity.domain.Rounds;
import com.xu.majiangcommunity.dto.PageResult;
import com.xu.majiangcommunity.service.SecurityUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class RoundService {

    @Autowired
    private RoundMapper roundMapper;


    @Autowired
    private SecurityUserService securityUserService;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addRound(Rounds round) {
        securityUserService.plusNewRound(round.getArticleId());
        roundMapper.addRound(round);
    }

    public int thumpUp(Integer rid) {
        int i = roundMapper.thumpUp(rid);
        return i;
    }

    public PageResult<List<Rounds>> findRoundByUid(String id, Integer page) {
        PageHelper.startPage(page, 10);
        List<Rounds> rounds = roundMapper.findRoundByUid(id);
        PageInfo<Rounds> roundPageInfo = new PageInfo<>(rounds);
        PageResult<List<Rounds>> result = new PageResult<>();
        result.setPageNum(roundPageInfo.getPageNum());
        result.setData(rounds);
        result.setTotal((int) roundPageInfo.getTotal());
        ArrayList<Integer> pages = null;
        if (page == 1 && roundPageInfo.getPages() >= 5) {
            pages = CollectionUtil.newArrayList(1, 2, 3, 4, 5);
        } else if (roundPageInfo.getPages() <= 5) {
            pages = new ArrayList<Integer>();
            for (int i = 1; i <= roundPageInfo.getPages(); i++) {
                pages.add(i);
            }
        } else {
            pages = new ArrayList<Integer>();
            for (int i = page - 1; i <= page + 3; i++) {
                pages.add(i);
            }
        }
        result.setPages(pages);
        return result;
    }

    public int countMyRound(String accountId) {
        return roundMapper.countMyRound(accountId);
    }
}
