package com.xu.majiangcommunity.service;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xu.majiangcommunity.dao.RoundMapper;
import com.xu.majiangcommunity.domain.Rounds;
import com.xu.majiangcommunity.dto.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoundService {

    @Autowired
    private RoundMapper roundMapper;

    @Autowired
    private UserService userService;

    public void addRound(Rounds round) {
        userService.plusNewRound(round.getArticleId());
        roundMapper.addRound(round);
    }

    public int thumpUp(Integer rid) {
        int i = roundMapper.thumpUp(rid);
        return i;
    }

    public PageResult<List<Rounds>> findRoundByUid(Integer id, Integer page) {
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
}
