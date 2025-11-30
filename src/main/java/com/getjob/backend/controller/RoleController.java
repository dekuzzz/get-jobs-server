package com.getjob.backend.controller;

import com.getjob.backend.annotation.RequireAuth;
import com.getjob.backend.common.ApiResponse;
import com.getjob.backend.common.ResultCode;
import com.getjob.backend.dto.RoleDTO;
import com.getjob.backend.service.impl.RoleServiceImpl;
import com.getjob.backend.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RoleController {

    private final RoleServiceImpl roleService;

    /**
     * 搜索授权（根据全名或邮箱搜索招聘者）
     */
    @GetMapping("/roles")
    @RequireAuth
    public ApiResponse<RoleDTO.SearchRolesResponse> searchRoles(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String emailAddress) {
        try {
            String userId = UserContext.getUserId();
            RoleDTO.SearchRolesResponse response;
            
            if ((fullName != null && !fullName.isEmpty()) || (emailAddress != null && !emailAddress.isEmpty())) {
                response = roleService.searchRolesByNameOrEmail(userId, fullName, emailAddress);
            } else {
                response = roleService.searchRoles(userId);
            }
            
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(ResultCode.FORBIDDEN.getCode(), e.getMessage());
        }
    }

    /**
     * 搜索已有授权（已加入公司的招聘者）
     */
    @GetMapping("/roles/exists")
    @RequireAuth
    public ApiResponse<RoleDTO.SearchRolesResponse> searchExistingRoles() {
        try {
            String userId = UserContext.getUserId();
            RoleDTO.SearchRolesResponse response = roleService.searchExistingRoles(userId);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(ResultCode.FORBIDDEN.getCode(), e.getMessage());
        }
    }

    /**
     * 添加授权
     */
    @PostMapping("/roles")
    @RequireAuth
    public ApiResponse<Void> addRoles(@RequestBody RoleDTO.AddRoleRequest request) {
        try {
            String userId = UserContext.getUserId();
            roleService.addRoles(userId, request);
            return ApiResponse.success("添加成功", null);
        } catch (Exception e) {
            return ApiResponse.error(ResultCode.BAD_REQUEST.getCode(), e.getMessage());
        }
    }

    /**
     * 删除授权
     */
    @DeleteMapping("/roles/{recruiterId}")
    @RequireAuth
    public ApiResponse<Void> deleteRole(@PathVariable Long recruiterId) {
        try {
            String userId = UserContext.getUserId();
            roleService.deleteRole(userId, recruiterId);
            return ApiResponse.success("删除成功", null);
        } catch (Exception e) {
            return ApiResponse.error(ResultCode.BAD_REQUEST.getCode(), e.getMessage());
        }
    }
}
