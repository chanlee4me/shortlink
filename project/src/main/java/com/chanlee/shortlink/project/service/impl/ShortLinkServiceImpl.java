package com.chanlee.shortlink.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chanlee.shortlink.project.dao.entity.ShortLinkDO;
import com.chanlee.shortlink.project.dao.mapper.ShortLinkMapper;
import com.chanlee.shortlink.project.service.ShortLinkService;
import org.springframework.stereotype.Service;

/**
 * 短链接接口实现层
 */
@Service
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {
}
