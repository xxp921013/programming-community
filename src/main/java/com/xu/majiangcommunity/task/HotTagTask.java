package com.xu.majiangcommunity.task;


import cn.hutool.core.bean.BeanUtil;
import com.xu.majiangcommunity.dao.ArticleMapper;
import com.xu.majiangcommunity.domain.Article;
import com.xu.majiangcommunity.domain.HotArticle;
import com.xu.majiangcommunity.domain.Tag;
import com.xu.majiangcommunity.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Slf4j
public class HotTagTask {
    private static final Long DAYS = 86400000L;
    private static final String HOTARTICLE = "HotArticle:";
    private static final String HOT_TAG = "HotTag:";

    @Autowired
    private ArticleService articleService;
    @Autowired
    private RedisTemplate<String, Serializable> redisCacheTemplate;

    @Scheduled(cron = "0/60 * * * * * ")
    public void getHotTask() {
        log.info("热门文章定时任务执行");
        System.out.println("热门文章定时任务执行");
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

    // @Scheduled(cron = "0/60 * * * * * ")
    @Scheduled(cron = "* * 0/12 * * ? ")
    public void getHotTags() {
        log.info("热门标签定时任务执行");
        System.out.println("热门标签定时任务执行");
        long l = System.currentTimeMillis();
        long t = l - (DAYS * 7);
        List<Article> lastDayArticle = articleService.getLastDayArticle(t);
        HashMap<String, Integer> tags = new HashMap<>();
        for (Article article : lastDayArticle) {
            String tags1 = article.getTags();
            String[] split = tags1.split(",");
            for (String s : split) {
                if (StringUtils.isNotBlank(s)) {
                    tags.put(s, tags.getOrDefault(s, 0) + 1);
                }
            }
        }
        BoundZSetOperations<String, Serializable> zSetOps = redisCacheTemplate.boundZSetOps(HOT_TAG);
        Tag tag = null;
        for (Map.Entry<String, Integer> stringIntegerEntry : tags.entrySet()) {
            tag = new Tag(stringIntegerEntry.getKey());
            zSetOps.add(tag, stringIntegerEntry.getValue());
        }
    }

}
