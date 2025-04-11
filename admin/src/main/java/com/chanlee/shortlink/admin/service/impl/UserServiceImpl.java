package com.chanlee.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chanlee.shortlink.admin.common.convention.exception.ClientException;
import com.chanlee.shortlink.admin.common.convention.exception.ServiceException;
import com.chanlee.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.chanlee.shortlink.admin.dao.entity.UserDO;
import com.chanlee.shortlink.admin.dao.mapper.UserMapper;
import com.chanlee.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.chanlee.shortlink.admin.dto.resp.UserRespDTO;
import com.chanlee.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 用户接口实现层
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {
    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;

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
        if(hasUsername(requestParam.getUsername())){
            throw new ClientException(UserErrorCodeEnum.USER_NAME_EXIST);
        }
        UserDO userDO = BeanUtil.copyProperties(requestParam, UserDO.class);
        int insert = baseMapper.insert(userDO);
        if(insert < 1){
            throw new ClientException(UserErrorCodeEnum.USER_SAVE_ERROR);
        }
        userRegisterCachePenetrationBloomFilter.add(requestParam.getUsername());
    }

}
