package com.xu.majiangcommunity.mq;

import cn.hutool.core.bean.BeanUtil;
import com.rabbitmq.client.Channel;
import com.xu.majiangcommunity.constant.MqConstant;
import com.xu.majiangcommunity.dao.ArticleRepo;
import com.xu.majiangcommunity.domain.Article;
import com.xu.majiangcommunity.domain.ArticleEs;
import com.xu.majiangcommunity.domain.Tag;
import com.xu.majiangcommunity.dto.ArticleDTO;
import com.xu.majiangcommunity.dto.TagDto;
import com.xu.majiangcommunity.service.TagService;
import com.xu.majiangcommunity.service.impl.ArticleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Component
public class ArticleMq {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleRepo articleRepo;
    private static final Long DAYS = 86400000L;
    private static final String HOT_TAG = "HotTag:";
    @Autowired
    private RedisTemplate<String, Serializable> redisCacheTemplate;
    @Autowired
    private TagService tagService;

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "article.es.add.queue", durable = "true")
            , exchange = @Exchange(name = MqConstant.ARTICLE_EXCHANGE, type = ExchangeTypes.TOPIC), key = {MqConstant.ARTICLE_ROUTING_KEY_ADD}))
    public void listenItemAddAndUpdate(Integer id, Message message, Channel channel) {
        //手动ack
        final long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            ArticleDTO oneById = articleService.findOneById(id);
            ArticleEs articleEs = new ArticleEs();
            BeanUtil.copyProperties(oneById, articleEs);
            articleEs.setUserImg(oneById.getUser().getImage());
            articleEs.setUsername(oneById.getUser().getUsername());
            articleEs.setUserId(oneById.getUser().getId());
            articleRepo.save(articleEs);
            //操作成功,手动ack
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            //出现异常,重新将消息压会队列
            try {
                channel.basicRecover();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    //手动刷新热门标签
    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "hot.tag.refresh.queue", durable = "true")
            , exchange = @Exchange(name = MqConstant.Hot_TAG_EXCHANGE, type = ExchangeTypes.TOPIC), key = {MqConstant.Tag_ROUTING_KEY_REFRESH}))
    public void listenHotTagRefresh(boolean flag, Message message, Channel channel) {
        System.out.println("接收到热门标签刷新消息!!!!!!!!!!!!");
        final long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            if (flag) {
                long l = System.currentTimeMillis();
                long t = l - (DAYS * 7);
                List<Article> lastDayArticle = articleService.getLastDayArticle(t);
                HashMap<String, Integer> tags = new HashMap<>();
                HashSet<String> set = new HashSet<>();
                for (Article article : lastDayArticle) {
                    String tags1 = article.getTags();
                    String[] split = tags1.split(",");
                    for (String s : split) {
                        if (StringUtils.isNotBlank(s)) {
                            tags.put(s, tags.getOrDefault(s, 0) + 1);
                            set.add(s);
                        }
                    }
                }
                List<Tag> tagList = tagService.getNameIn(set);
                HashMap<String, Long> databaseMap = new HashMap<>();
                for (Tag tag : tagList) {
                    databaseMap.put(tag.getName(), tag.getWeight());
                }
                BoundZSetOperations<String, Serializable> zSetOps = redisCacheTemplate.boundZSetOps(HOT_TAG);
                if (zSetOps.size() > 0) {
                    zSetOps.removeRange(0, zSetOps.size());
                }
                TagDto tagDto = null;
                for (Map.Entry<String, Integer> stringIntegerEntry : tags.entrySet()) {
                    long score = stringIntegerEntry.getValue() + databaseMap.get(stringIntegerEntry.getKey());
                    tagDto = new TagDto(stringIntegerEntry.getKey(), score);
                    zSetOps.add(tagDto, score);
                }
            }
            System.out.println("热门标签刷新成功!!!!!!!!!!!!");
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            try {
                channel.basicRecover();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
