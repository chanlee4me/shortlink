package com.chanlee.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chanlee.shortlink.project.common.convention.exception.ClientException;
import com.chanlee.shortlink.project.dao.entity.ShortLinkDO;
import com.chanlee.shortlink.project.dao.mapper.ShortLinkMapper;
import com.chanlee.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.chanlee.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.chanlee.shortlink.project.service.ShortLinkService;
import com.chanlee.shortlink.project.util.HashUtil;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.springframework.stereotype.Service;

/**
 * 短链接接口实现层
 */
@Service
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {
    private final RBloomFilter<String> shortlinkCreateCachePenetrationBloomFilter;

    public ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam) {

        String shortLinkSuffix = generateSuffix(requestParam);
        String fullShortUrl = requestParam.getDomain() + "/" +  shortLinkSuffix;
        //更新数据库
        ShortLinkDO shortLinkDO = BeanUtil.toBean(requestParam, ShortLinkDO.class);
        shortLinkDO.setShortUri(shortLinkSuffix);
        shortLinkDO.setFullShortUrl(fullShortUrl);
        //TODO 捕获数据库唯一索引异常
        baseMapper.insert(shortLinkDO);
        //TODO更新 Redis 缓存

        //更新布隆过滤器
        shortlinkCreateCachePenetrationBloomFilter.add(fullShortUrl);

        ShortLinkCreateRespDTO respDTO = ShortLinkCreateRespDTO.builder()
                .gid(requestParam.getGid())
                .originUrl(requestParam.getOriginUrl())
                .fullShortUrl(fullShortUrl)
                .build();
        return respDTO;
    }

    public String generateSuffix(ShortLinkCreateReqDTO requestParm) {
        int count = 0;
        String shortUri;
        do{
            String originUrl = requestParm.getOriginUrl();
            String uuid = UUID.randomUUID().toString();
            shortUri = HashUtil.hashToBase62(originUrl + uuid);
            if(!shortlinkCreateCachePenetrationBloomFilter.contains(requestParm.getDomain() + "/" + shortUri)){
                break;
            }
            count++;
        }while(count < 10);
        if(count == 10)
            throw new ClientException("短链接生成失败");
        else
            return shortUri;
    }
}
