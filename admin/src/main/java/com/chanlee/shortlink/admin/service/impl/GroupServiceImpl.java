package com.chanlee.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chanlee.shortlink.admin.dao.entity.GroupDO;
import com.chanlee.shortlink.admin.dao.mapper.GroupMapper;
import com.chanlee.shortlink.admin.dto.req.ShortLinkGroupSaveReqDTO;
import com.chanlee.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;
import com.chanlee.shortlink.admin.service.GroupService;
import com.chanlee.shortlink.admin.util.RandomGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 短链接分组接口实现层
 */
@Slf4j
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {

    public void saveGroup(ShortLinkGroupSaveReqDTO requestParam) {
        String gid;
        do{
            gid = RandomGenerator.generateRandom();
        }while(!isValidGid(gid));
        GroupDO groupDO = GroupDO.builder()
                .gid(gid)
                .sortOrder(0)
                .name(requestParam.getName())
                .build();
        baseMapper.insert(groupDO);
    }

    public boolean isValidGid(String gid){
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid, gid)
                //todo 设置用户名
                .eq(GroupDO::getUsername, null);
        GroupDO groupDO = baseMapper.selectOne(queryWrapper);
        return groupDO == null;
    }

    public List<ShortLinkGroupRespDTO> listGroup() {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getDelFlag, 0)
                .orderByDesc(GroupDO::getSortOrder, GroupDO::getUpdateTime);
        queryWrapper.isNull(GroupDO::getUsername);
        List<GroupDO> list = baseMapper.selectList(queryWrapper);
        return BeanUtil.copyToList(list, ShortLinkGroupRespDTO.class);
    }

}
