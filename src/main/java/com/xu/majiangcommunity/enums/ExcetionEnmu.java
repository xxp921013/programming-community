package com.xu.majiangcommunity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Administrator
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExcetionEnmu {
    TEST_THROW(200, "抛出测试"),
    USERNAME_PASSWORD_EMPTY(10002, "用户名密码为空,请重新输入"),
    REGISTRY_MAIL_VERIFY_ERROR(10003, "注册邮箱验证错误"),
    USERNAME_ALREADY_USED(10004, "用户名重复"),
    REGISTRY_FAILED(10005, "用户名重复"),
    USERNAME_IS_EXISTS(500, "用户名重复,请重新输入"),
    FILE_TYPE_ERROR(500, "文件类型错误"),
    USER_NO_LOGIN(10001, "用户未登录"),
    FILE_SERVER_ERROR(500, "文件服务错误");
    private int code;
    private String message;
}
