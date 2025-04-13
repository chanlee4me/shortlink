package com.chanlee.shortlink.admin.controller;

import com.chanlee.shortlink.admin.common.convention.result.Result;
import com.chanlee.shortlink.admin.common.convention.result.Results;
import com.chanlee.shortlink.admin.dto.resp.JwtRespDTO;
import com.chanlee.shortlink.admin.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于测试的工具控制层
 */
@RestController
@RequiredArgsConstructor
public class UtilController {

    private final JwtService jwtService;
    /**
     * 查询当前用户上文中的信息
     */
    @GetMapping("/api/shortlink/v1/user/usercontext")
    public Result<JwtRespDTO> getUserContext(@RequestHeader("token") String token){
        return Results.success(jwtService.getUserContext(token));
    }
}
