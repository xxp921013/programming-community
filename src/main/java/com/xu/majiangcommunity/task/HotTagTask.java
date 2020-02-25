package com.xu.majiangcommunity.task;


import cn.hutool.core.bean.BeanUtil;
import com.xu.majiangcommunity.dao.ArticleMapper;
import com.xu.majiangcommunity.domain.Article;
import com.xu.majiangcommunity.domain.HotArticle;
import com.xu.majiangcommunity.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Time;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class HotTagTask {
    private static final Long DAYS = 86400000L;
    private static final String HOTARTICLE = "HotArticle:";
    @Autowired
    private ArticleService articleService;
    @Autowired
    private RedisTemplate<String, Serializable> redisCacheTemplate;

    //@Scheduled(cron = "0/60 * * * * * ")
    public void getHotTask() {
        System.out.println("定时任务执行");
        long l = System.currentTimeMillis();
        long t = l - DAYS;
        List<Article> lastDayArticle = articleService.getLastDayArticle(t);
        List<HotArticle> collect = lastDayArticle.parallelStream().map(article -> getHotArticle(article)).collect(Collectors.toList());
        BoundZSetOperations<String, Serializable> zSetOps = redisCacheTemplate.boundZSetOps(HOTARTICLE);
        for (HotArticle hotArticle : collect) {
            Long score = hotArticle.getWeights();
            hotArticle.setWeights((long) 0);
            zSetOps.add(hotArticle, score);
        }
    }

    public static HotArticle getHotArticle(Article article) {
        HotArticle hotArticle = new HotArticle();
        hotArticle.setArticleId(article.getId());
        hotArticle.setTittle(article.getTittle());
        Long weights = Long.valueOf(article.getViewCount() + (article.getCommentCount() * 10));
        hotArticle.setWeights(weights);
        return hotArticle;
    }

}
