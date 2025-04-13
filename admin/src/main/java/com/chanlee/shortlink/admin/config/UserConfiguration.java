package com.chanlee.shortlink.admin.config;

import com.chanlee.shortlink.admin.common.biz.user.UserTransmitFilter;
import com.chanlee.shortlink.admin.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用户配置自动装配
 */
@Configuration
@RequiredArgsConstructor
public class UserConfiguration {
    private final JwtUtil jwtUtil; // 注入 JWT 工具类
    /**
     * 用户信息传递过滤器
     */
    @Bean
    public FilterRegistrationBean<UserTransmitFilter> globalUserTransmitFilter() {
        FilterRegistrationBean<UserTransmitFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new UserTransmitFilter(jwtUtil));
        registration.addUrlPatterns("/*");
        registration.setOrder(0);
        return registration;
    }
}
