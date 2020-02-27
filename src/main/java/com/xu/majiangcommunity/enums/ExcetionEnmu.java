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
    USERNAME_PASSWORD_EMPTY(500, "用户名密码为空,请重新输入"),
    USERNAME_IS_EXISTS(500, "用户名重复,请重新输入"),
    FILE_TYPE_ERROR(500, "文件类型错误"),
    FILE_SERVER_ERROR(500, "文件服务错误");

    private int code;
    private String message;
}
