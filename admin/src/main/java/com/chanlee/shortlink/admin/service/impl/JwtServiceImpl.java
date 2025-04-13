package com.chanlee.shortlink.admin.service.impl;

import com.chanlee.shortlink.admin.dto.resp.JwtRespDTO;
import com.chanlee.shortlink.admin.service.JwtService;
import com.chanlee.shortlink.admin.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtUtil jwtUtil;

    public JwtRespDTO getUserContext(String token) {
        String username = jwtUtil.getUsernameFromToken(token);
        String userId = jwtUtil.getUserIdFromToken(token);
        String realName = jwtUtil.getUserRealNameFromToken(token);
        JwtRespDTO result = JwtRespDTO.builder()
                .username(username)
                .userId(userId)
                .realName(realName)
                .build();
        return result;
    }
}
