package com.chanlee.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chanlee.shortlink.admin.common.constant.RedisCacheConstant;
import com.chanlee.shortlink.admin.common.convention.exception.ClientException;
import com.chanlee.shortlink.admin.common.convention.exception.ServiceException;
import com.chanlee.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.chanlee.shortlink.admin.dao.entity.UserDO;
import com.chanlee.shortlink.admin.dao.mapper.UserMapper;
import com.chanlee.shortlink.admin.dto.req.UserLoginReqDTO;
import com.chanlee.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.chanlee.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.chanlee.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.chanlee.shortlink.admin.dto.resp.UserRespDTO;
import com.chanlee.shortlink.admin.service.UserService;
import com.chanlee.shortlink.admin.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


/**
 * 用户接口实现层
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {
    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    private final RedissonClient redissonClient;
    private final JwtUtil jwtUtil;
    private final StringRedisTemplate stringRedisTemplate;

    public UserRespDTO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if(userDO == null)
            throw new ServiceException(UserErrorCodeEnum.USER_NULL);
        UserRespDTO result = new UserRespDTO();
        BeanUtils.copyProperties(userDO, result);
        return result;
    }

    public boolean hasUsername(String username) {
        return userRegisterCachePenetrationBloomFilter.contains(username);
    }

    public void register(UserRegisterReqDTO requestParam) {
        if (hasUsername(requestParam.getUsername())) {
            throw new ClientException(UserErrorCodeEnum.USER_NAME_EXIST);
        }
        UserDO userDO = BeanUtil.copyProperties(requestParam, UserDO.class);
        //获取分布式锁，防止大量请求重复创建同一个用户名
        RLock lock = redissonClient.getLock(RedisCacheConstant.LOCK_USER_REGISTER_KEY + requestParam.getUsername());
        try {
            if (lock.tryLock()) {
                int insert = baseMapper.insert(userDO);
                if (insert < 1) {
                    throw new ClientException(UserErrorCodeEnum.USER_SAVE_ERROR);
                }
                userRegisterCachePenetrationBloomFilter.add(requestParam.getUsername());
                return;
            }
            throw new ClientException(UserErrorCodeEnum.USER_NAME_EXIST);
        } finally {
            lock.unlock();
        }
    }

    public void update(UserUpdateReqDTO requestParam) {
        LambdaUpdateWrapper<UserDO> wrapper = Wrappers.lambdaUpdate(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername());
        baseMapper.update(BeanUtil.toBean(requestParam, UserDO.class), wrapper);
    }

    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        //TODO 如果用户已经登录，那么不用再分配新的令牌，而且刷新现有的令牌
        if(!hasUsername(requestParam.getUsername())){
            throw new ClientException(UserErrorCodeEnum.USER_NOT_EXIST);
        }
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername())
                .eq(UserDO::getPassword, requestParam.getPassword());
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if(userDO == null)
            throw new ClientException(UserErrorCodeEnum.USER_NOT_EXIST);
        // 登录成功后，生成jwt令牌
        String token = jwtUtil.createJwtToken(userDO.getUsername(), userDO.getId().toString(), userDO.getRealName());
        UserLoginRespDTO respDTO = new UserLoginRespDTO();
        respDTO.setToken(token);
        return respDTO;
    }

    public Void logout(String token) {
        if(stringRedisTemplate.hasKey(RedisCacheConstant.USER_LOG_OUT_KEY + token)){
            throw new ClientException(UserErrorCodeEnum.USER_ALREADY_LOG_OUT);
        }else{
            stringRedisTemplate.opsForValue().set(RedisCacheConstant.USER_LOG_OUT_KEY + token, "0", 30, TimeUnit.MINUTES);
            return null;
        }
    }
}
