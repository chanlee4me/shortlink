package com.chanlee.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chanlee.shortlink.admin.dao.entity.GroupDO;
import com.chanlee.shortlink.admin.dto.req.ShortLinkGroupSaveReqDTO;
import com.chanlee.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;

import java.util.List;

/**
 * 短链接分组接口层
 */
public interface GroupService extends IService<GroupDO> {
    /**
     * 新建短链接分组
     * @param requestParam
     */
    public void saveGroup(ShortLinkGroupSaveReqDTO requestParam);

    /**
     * 查询短链接分组
     * @return
     */
    public List<ShortLinkGroupRespDTO> listGroup();
}
