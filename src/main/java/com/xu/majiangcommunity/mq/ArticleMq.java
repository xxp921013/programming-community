package com.xu.majiangcommunity.mq;

import cn.hutool.core.bean.BeanUtil;
import com.rabbitmq.client.Channel;
import com.xu.majiangcommunity.constant.MqConstant;
import com.xu.majiangcommunity.dao.ArticleRepo;
import com.xu.majiangcommunity.domain.ArticleEs;
import com.xu.majiangcommunity.dto.ArticleDTO;
import com.xu.majiangcommunity.service.impl.ArticleService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ArticleMq {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleRepo articleRepo;

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
}
