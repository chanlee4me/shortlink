package com.chanlee.shortlink.admin.service;

import com.chanlee.shortlink.admin.dto.resp.JwtRespDTO;

/**
 * jwt接口层
 */
public interface JwtService {
    /**
     * 获取用户上下文信息
     * @return
     */
    public JwtRespDTO getUserContext(String token);
}
