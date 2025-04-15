package com.chanlee.shortlink.admin.controller;

import com.chanlee.shortlink.admin.common.convention.result.Result;
import com.chanlee.shortlink.admin.common.convention.result.Results;
import com.chanlee.shortlink.admin.dto.req.ShortLinkGroupSaveReqDTO;
import com.chanlee.shortlink.admin.dto.req.ShortLinkGroupSortReqDTO;
import com.chanlee.shortlink.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import com.chanlee.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;
import com.chanlee.shortlink.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短链接分组控制层
 */
@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    /**
     * 新建短链接分组
     * @param requestParam
     * @return
     */
    @PostMapping("/api/shortlink/admin/v1/group")
    public Result<Void> saveGroup(@RequestBody ShortLinkGroupSaveReqDTO requestParam){
        groupService.saveGroup(requestParam);
        return Results.success();
    }

    /**
     * 查询短链接分组
     * @return
     */
    @GetMapping("/api/shortlink/admin/v1/group")
    public Result<List<ShortLinkGroupRespDTO>> listGroup(){
        return Results.success(groupService.listGroup());
    }

    /**
     * 修改短链接分组名称
     * @return
     */
    @PutMapping("/api/shortlink/admin/v1/group")
    public Result<Void> updateGroup(@RequestBody ShortLinkGroupUpdateReqDTO requestParam){
        groupService.updateGroup(requestParam);
        return Results.success();
    }

    /**
     * 删除短链接分组
     * @param gid
     * @return
     */
    @DeleteMapping("/api/shortlink/admin/v1/group")
    public Result<Void> deleteGroup(@RequestParam String gid){
        groupService.deleteGroup(gid);
        return Results.success();
    }

    /**
     * 修改短链接分组排序
     * @param requestParam
     * @return
     */
    @PostMapping("/api/shortlink/admin/v1/group/sort")
    public Result<Void> sortGroup(@RequestBody List<ShortLinkGroupSortReqDTO> requestParam){
        groupService.sortGroup(requestParam);
        return Results.success();
    }
}
