package com.xu.majiangcommunity.controller;

import com.xu.majiangcommunity.domain.Rounds;
import com.xu.majiangcommunity.service.ArticleService;
import com.xu.majiangcommunity.service.RoundService;
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
        rounds.setGmtCreate(System.currentTimeMillis());
        rounds.setGmtModified(System.currentTimeMillis());
        roundService.addRound(rounds);
        articleService.commentArticle(rounds.getArticleId());
        return "redirect:/articleDetail?id=" + rounds.getArticleId();
    }

    @PutMapping("/thumpUp")
    @ResponseBody
    public boolean thumpUp(@RequestParam("rid") Integer rid) {
        int i = roundService.thumpUp(rid);
        if (i >= 1) {
            return true;
        }
        return false;
    }
}
