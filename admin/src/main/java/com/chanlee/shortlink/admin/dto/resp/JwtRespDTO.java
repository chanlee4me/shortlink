package com.chanlee.shortlink.admin.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Jwt 请求返回参数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtRespDTO {
    /**
     * 用户名
     */
    private String username;

    /**
     * 用户 ID
     */
    private String userId;

    /**
     * 真实姓名
     */
    private String realName;
}
