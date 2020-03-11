package com.xu.majiangcommunity.controller;

import com.xu.majiangcommunity.UserException;
import com.xu.majiangcommunity.domain.Rounds;
import com.xu.majiangcommunity.domain.SecurityUser;
import com.xu.majiangcommunity.dto.BaseResponseBody;
import com.xu.majiangcommunity.enums.ExcetionEnmu;
import com.xu.majiangcommunity.interceptor.UserInterceptor;
import com.xu.majiangcommunity.service.impl.ArticleService;
import com.xu.majiangcommunity.service.impl.RoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RoundController {
    @Autowired
    private RoundService roundService;
    @Autowired
    private ArticleService articleService;

    @PostMapping("/addRound")
    public String addRound(Rounds rounds) {
        SecurityUser user = UserInterceptor.getUser();
        if (user == null) {
            throw new UserException(ExcetionEnmu.TEST_THROW);
        }
        rounds.setImage(user.getImage());
        rounds.setName(user.getUsername());
        rounds.setRoundCreator(user.getUsername());
        rounds.setGmtCreate(System.currentTimeMillis());
        rounds.setGmtModified(System.currentTimeMillis());
        roundService.addRound(rounds);
        articleService.commentArticle(rounds.getArticleId());
        return "redirect:/articleDetail?id=" + rounds.getArticleId();
    }

    @PutMapping("/thumpUp")
    @ResponseBody
    public BaseResponseBody<Boolean> thumpUp(@RequestParam("rid") String rid) {
        System.out.println(rid);
        int i = roundService.thumpUp(Integer.valueOf(rid));

        if (i >= 1) {
            return new BaseResponseBody<Boolean>(200, "点赞成功", true);

        }
        return new BaseResponseBody<Boolean>(500, "点赞失败", false);
    }
}
