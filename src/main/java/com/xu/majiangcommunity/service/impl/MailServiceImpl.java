package com.xu.majiangcommunity.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.xu.majiangcommunity.dto.BaseResponseBody;
import com.xu.majiangcommunity.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MailServiceImpl implements MailService {
    @Autowired
    private StringRedisTemplate srt;
    public static final String MAIL_REGISTRY_PREFIX = "mailRegistry:";
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    MailProperties mailProperties;

    @Override
    public boolean sendRegistryCode(String mail) {
        String key = MAIL_REGISTRY_PREFIX + mail;
        int i = RandomUtil.randomInt(100000, 999999);
        ValueOperations<String, String> ops = srt.opsForValue();
        ops.set(key, String.valueOf(i), 120, TimeUnit.SECONDS);
        Context context = new Context();
        HashMap<String, Object> map = new HashMap<>();
        map.put("registryCode", i);
        context.setVariables(map);
        String registryMail = templateEngine.process("registryMail", context);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(mail);
            helper.setFrom(mailProperties.getUsername());
            helper.setSubject("开发社区-用户注册");
            helper.setSentDate(new Date());
            helper.setText(registryMail, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("邮件发送失败,邮箱地址:{}", mail);
            return false;
        }
        return true;
    }

    @Override
    public boolean verifyRegisry(String mail, String code) {
        String key = MAIL_REGISTRY_PREFIX + mail;
        if (!srt.hasKey(key)) {
            return false;
        }
        ValueOperations<String, String> ops = srt.opsForValue();
        String s = ops.get(key);
        if (s.equals(code)) {
            return true;
        }
        return false;
    }
}
