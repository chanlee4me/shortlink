package com.chanlee.shortlink.admin.common.biz.user;

import cn.hutool.core.util.StrUtil;
import com.chanlee.shortlink.admin.common.convention.exception.ClientException;
import com.chanlee.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.chanlee.shortlink.admin.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

/**
 * 用户信息传输过滤器
 *
 */
@RequiredArgsConstructor
public class UserTransmitFilter implements Filter {

    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpServletRequest.getRequestURI();

        // 如果是登录路径，直接跳过用户信息处理
        if (requestURI.equals("/api/shortlink/v1/user/login")){ // 根据你的实际登录路径调整
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String token = httpServletRequest.getHeader("token");
        //检验 token 是否合法
        if(StrUtil.isBlank(token)){
            throw new ClientException(UserErrorCodeEnum.TOKEN_IS_NULL);
        }
        if(!jwtUtil.verifyToken(token)){
            throw new ClientException(UserErrorCodeEnum.INVALID_TOKEN);
        }
        String userId = jwtUtil.getUserIdFromToken(token);
        String username = jwtUtil.getUsernameFromToken(token);
        String realName = jwtUtil.getUserRealNameFromToken(token);
        UserInfoDTO userInfoDTO = new UserInfoDTO(userId, username, realName);
        UserContext.setUser(userInfoDTO);

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            UserContext.removeUser();
        }
    }
}