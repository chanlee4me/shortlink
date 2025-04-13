package com.chanlee.shortlink.admin.dto.req;

import lombok.Data;

/**
 * 短链接分组修改参数
 */
@Data
public class ShortLinkGroupSortReqDTO {
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 排序顺序
     */
    private Integer sortOrder;
}
