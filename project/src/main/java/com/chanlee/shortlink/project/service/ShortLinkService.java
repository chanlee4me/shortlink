package com.chanlee.shortlink.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chanlee.shortlink.project.dao.entity.ShortLinkDO;
import com.chanlee.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.chanlee.shortlink.project.dto.resp.ShortLinkCreateRespDTO;

/**
 * 短链接接口层
 */
public interface ShortLinkService extends IService<ShortLinkDO> {

    /**
     * 创建短链接
     * @param requestParam 短链接创建请求对象
     */
    ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam);

}
