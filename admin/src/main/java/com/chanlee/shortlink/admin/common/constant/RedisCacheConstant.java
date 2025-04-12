package com.chanlee.shortlink.admin.common.constant;

public class RedisCacheConstant {
    /**
     * 用户注册分布式锁
     */
    public static final String LOCK_USER_REGISTER_KEY = "shortlink:lock_user_register_key:";
    /**
     * 用户 token 黑名单标识（注销时添加）
     */
    public static final String USER_LOG_OUT_KEY = "shortlink:user_log_out_key:";
}
