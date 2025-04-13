package com.chanlee.shortlink.admin.common.enums;

import com.chanlee.shortlink.admin.common.convention.errorcode.IErrorCode;

/**
 * 用户错误码
 * TODO 后期把它和BaseErrorCode.java进行整合
 */
public enum UserErrorCodeEnum implements IErrorCode {
    USER_NULL("B000200", "用户记录不存在"),

    USER_NAME_EXIST("B000201", "用户名已存在"),

    USER_EXIST("B000202", "用户记录已存在"),

    USER_SAVE_ERROR("B000203","用户记录新增失败"),

    USER_LOGIN_EXIST("B000204", "用户已登录"),

    USER_NOT_EXIST("B000205", "用户已登录"),

    USER_ALREADY_LOG_OUT("B000206", "用户已注销"),

    INVALID_TOKEN("B000207", "token非法"),
    TOKEN_IS_NULL("B000208", "token为空");

    private final String code;

    private final String message;

    UserErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}

