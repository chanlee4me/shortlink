package com.chanlee.shortlink.admin.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.chanlee.shortlink.admin.common.constant.JwtClaimsConstant;
import com.chanlee.shortlink.admin.config.JwtConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    /**
     * jwt 配置类
     */
    private final JwtConfiguration jwtConfig;

    /**
     * 创建 JWT token
     *
     * @param username 用户名
     * @return token 字符串
     */
    public String createJwtToken(String username) {

        // 获取签名器
        JWTSigner signer = JWTSignerUtil.hs256(jwtConfig.getSecretKey().getBytes());

        // 设置过期时间
        Date expiration = DateUtil.offsetSecond(new Date(), (int) jwtConfig.getTtl());

        // 设置payload
        Map<String, Object> payload = new HashMap<>();
        payload.put(JwtClaimsConstant.USERNAME, username);

        // 构建JWT
        return JWT.create()
                .addPayloads(payload)
                .setExpiresAt(expiration)
                .setSigner(signer)
                .sign();
    }

    /**
     * 验证 Token 是否有效
     *
     * @param token token字符串
     * @return 是否有效
     */
    public boolean verifyToken(String token) {
        try {
            JWTSigner signer = JWTSignerUtil.hs256(jwtConfig.getSecretKey().getBytes());
            JWT jwt = JWT.of(token);

            // 验证签名
            if (!jwt.setSigner(signer).verify()) {
                return false;
            }

            // 验证过期时间
            JWTValidator.of(jwt).validateDate();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从 Token 中获取用户名
     *
     * @param token token字符串
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        JWT jwt = JWT.of(token);
        return jwt.getPayload(JwtClaimsConstant.USERNAME).toString();
    }

    /**
     * 刷新 Token (重新生成)
     * @param token 旧token
     * @return 新token
     */
    public String refreshToken(String token) {
        String username = getUsernameFromToken(token);
        return createJwtToken(username);
    }
}
