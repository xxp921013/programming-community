package com.xu.majiangcommunity.service;

import org.springframework.stereotype.Service;

import javax.swing.text.StyledEditorKit;


public interface MailService {

    boolean sendRegistryCode(String mail);

    boolean verifyRegisry(String mail, String code);
}
