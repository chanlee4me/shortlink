package com.chanlee.shortlink.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.chanlee.shortlink.admin.common.convention.result.Result;
import com.chanlee.shortlink.admin.common.convention.result.Results;
import com.chanlee.shortlink.admin.dto.req.UserLoginReqDTO;
import com.chanlee.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.chanlee.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.chanlee.shortlink.admin.dto.resp.UserActualRespDTO;
import com.chanlee.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.chanlee.shortlink.admin.dto.resp.UserRespDTO;
import com.chanlee.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制层
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    @GetMapping("/api/shortlink/admin/v1/user/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable String username){
        return Results.success(userService.getUserByUsername(username));
    }
    /**
     * 根据用户名查询无脱敏用户信息
     * @param username
     * @return
     */
    @GetMapping("/api/shortlink/admin/v1/actual/user/{username}")
    public Result<UserActualRespDTO> getActualUserByUsername(@PathVariable String username){
        return Results.success(BeanUtil.copyProperties(userService.getUserByUsername(username), UserActualRespDTO.class));
    }
    /**
     * 查询用户名是否已经被注册
     */
    @GetMapping("/api/shortlink/admin/v1/user/has-username")
    public Result<Boolean> hasUsername(@RequestParam("username") String username){
        return Results.success(userService.hasUsername(username));
    }

    /**
     * 注册新用户
     * @param requestParam
     * @return
     */
    @PostMapping("/api/shortlink/admin/v1/user")
    public Result<Void> register(@RequestBody UserRegisterReqDTO requestParam){
        userService.register(requestParam);
        return Results.success();
    }

    /**
     * 修改用户个人信息
     * @param requestParam
     * @return
     */
    @PutMapping("/api/shortlink/admin/v1/user")
    public Result<Void> update(@RequestBody UserUpdateReqDTO requestParam){
        userService.update(requestParam);
        return Results.success();
    }

    /**
     * 用户登录
     * @param requestParam 请求参数
     * @return token
     */
    @PostMapping("/api/shortlink/admin/v1/user/login")
    public Result<UserLoginRespDTO> login(@RequestBody UserLoginReqDTO requestParam){
        return Results.success(userService.login(requestParam));
    }

    /**
     * 用户注销登录
     * @param token jwttoken
     * @return
     */
    @PostMapping("/api/shortlink/admin/v1/user/logout")
    public Result<Void> logout(String token){
         return Results.success(userService.logout(token));
    }
}
