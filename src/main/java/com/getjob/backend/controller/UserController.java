package com.getjob.backend.controller;

import com.getjob.backend.annotation.RequireAuth;
import com.getjob.backend.common.ApiResponse;
import com.getjob.backend.common.ResultCode;
import com.getjob.backend.dto.AuthDTO;
import com.getjob.backend.mapper.UserAccountMapper;
import com.getjob.backend.model.UserAccountEntity;
import com.getjob.backend.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserAccountMapper userAccountMapper;

    /**
     * 获取当前用户身份（role）
     * 接口地址: /api/user/profile
     * 请求方式: GET
     * 需要认证: 是（通过token）
     */
    @GetMapping("/user/profile")
    @RequireAuth
    public ApiResponse<AuthDTO.UserProfileResponse> getUserProfile() {
        try {
            String userId = UserContext.getUserId();
            
            // 从数据库查询用户信息
            UserAccountEntity user = userAccountMapper.selectById(Long.parseLong(userId));
            if (user == null) {
                return ApiResponse.error(ResultCode.NOT_FOUND.getCode(), "用户不存在");
            }
            
            AuthDTO.UserProfileResponse response = new AuthDTO.UserProfileResponse();
            response.setUserId(user.getUserId());
            response.setEmail(user.getEmail());
            response.setRole(user.getRole() != null ? user.getRole() : "job_seeker"); // 默认值
            
            return ApiResponse.success("获取用户信息成功", response);
        } catch (Exception e) {
            return ApiResponse.error(ResultCode.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * 切换用户身份（role）
     * 接口地址: /api/switch-role
     * 请求方式: POST
     * 需要认证: 是（通过token）
     * 请求参数: token + target_role
     */
    @PostMapping("/switch-role")
    @RequireAuth
    public ApiResponse<AuthDTO.SwitchRoleResponse> switchRole(
            @RequestBody AuthDTO.SwitchRoleRequest request) {
        try {
            String userId = UserContext.getUserId();
            
            // 验证target_role参数
            String targetRole = request.getTargetRole();
            if (targetRole == null || (!targetRole.equals("job_seeker") && !targetRole.equals("recruiters"))) {
                return ApiResponse.error(ResultCode.BAD_REQUEST.getCode(), "无效的角色类型，必须是 job_seeker 或 recruiters");
            }
            
            // 从数据库查询用户信息
            UserAccountEntity user = userAccountMapper.selectById(Long.parseLong(userId));
            if (user == null) {
                return ApiResponse.error(ResultCode.NOT_FOUND.getCode(), "用户不存在");
            }
            
            // 更新role字段
            user.setRole(targetRole);
            user.setUpdatedAt(Instant.now());
            userAccountMapper.updateById(user);
            
            AuthDTO.SwitchRoleResponse response = new AuthDTO.SwitchRoleResponse();
            response.setUserId(user.getUserId());
            response.setEmail(user.getEmail());
            response.setRole(targetRole);
            
            return ApiResponse.success("切换身份成功", response);
        } catch (Exception e) {
            return ApiResponse.error(ResultCode.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }
    }
}

