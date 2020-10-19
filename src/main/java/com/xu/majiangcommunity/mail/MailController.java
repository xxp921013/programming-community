package com.xu.majiangcommunity.mail;

import cn.hutool.core.util.StrUtil;
import com.xu.majiangcommunity.dto.BaseResponseBody;
import com.xu.majiangcommunity.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
@Slf4j
public class MailController {

    @Autowired
    private MailService mailService;

    @GetMapping("/sendRegistryMail")
    public BaseResponseBody sendMail(@RequestParam("mail") String mail) {
        BaseResponseBody responseBody = null;
        if (StrUtil.isBlank(mail)) {
            responseBody = new BaseResponseBody(500, "邮箱为空!");
            return responseBody;
        }
        if (mailService.sendRegistryCode(mail)) {
            responseBody = new BaseResponseBody(200, "发送成功!");
        } else {
            responseBody = new BaseResponseBody(500, "发送失败!");
            return responseBody;
        }
        return responseBody;
    }
}
