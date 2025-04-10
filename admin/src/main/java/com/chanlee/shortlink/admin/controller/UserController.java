package com.chanlee.shortlink.admin.controller;

import com.chanlee.shortlink.admin.common.convention.result.Result;
import com.chanlee.shortlink.admin.common.convention.result.Results;
import com.chanlee.shortlink.admin.dto.resp.UserRespDTO;
import com.chanlee.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理控制层
 */
@RestController
@RequestMapping("/api/shortlink/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    @GetMapping("{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable String username){
        return Results.success(userService.getUserByUsername(username));
    }
}
