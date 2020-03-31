package com.xu.majiangcommunity.mq;

import cn.hutool.core.bean.BeanUtil;
import com.xu.majiangcommunity.constant.MqConstant;
import com.xu.majiangcommunity.dao.ArticleRepo;
import com.xu.majiangcommunity.domain.ArticleEs;
import com.xu.majiangcommunity.dto.ArticleDTO;
import com.xu.majiangcommunity.service.impl.ArticleService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArticleMq {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleRepo articleRepo;

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "article.es.add.queue", durable = "true")
            , exchange = @Exchange(name = MqConstant.ARTICLE_EXCHANGE, type = ExchangeTypes.TOPIC), key = {MqConstant.ARTICLE_ROUTING_KEY_ADD}))
    public void listenItemAddAndUpdate(Integer id) {
        ArticleDTO oneById = articleService.findOneById(id);
        ArticleEs articleEs = new ArticleEs();
        BeanUtil.copyProperties(oneById, articleEs);
        articleEs.setUserImg(oneById.getUser().getImage());
        articleEs.setUsername(oneById.getUser().getUsername());
        articleEs.setUserId(oneById.getUser().getId());
        articleRepo.save(articleEs);
    }
}
