package com.chanlee.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chanlee.shortlink.admin.dao.entity.UserDO;
import com.chanlee.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.chanlee.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.chanlee.shortlink.admin.dto.resp.UserRespDTO;

/**
 * 用户接口层
 */
public interface UserService extends IService<UserDO> {
    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 用户返回实体
     */
    UserRespDTO getUserByUsername(String username);

    /**
     * 检查用户名是否已经被注册
     * @param username
     * @return 返回 true 表示用户已经被注册，返回 false表示用户未被注册
     */
    boolean hasUsername(String username);

    /**
     * 用户注册
     * @param requestParam
     */
    void register(UserRegisterReqDTO requestParam);

    /**
     * 修改用户个人信息
     * @param requestParam 修改用户请求参数
     */
    void update(UserUpdateReqDTO requestParam);
}
