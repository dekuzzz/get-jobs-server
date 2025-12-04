package com.getjob.backend.controller;

import com.getjob.backend.annotation.RequireAuth;
import com.getjob.backend.common.ApiResponse;
import com.getjob.backend.common.ResultCode;
import com.getjob.backend.dto.AuthDTO;
import com.getjob.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/user/send-verification-code")
    public ApiResponse<AuthDTO.SendVerificationCodeResponse> sendVerificationCode(
            @RequestBody AuthDTO.SendVerificationCodeRequest request) {
        try {
            AuthDTO.SendVerificationCodeResponse response = authService.sendVerificationCode(request);
            return ApiResponse.success("验证码发送成功", response);
        } catch (Exception e) {
            return ApiResponse.error(ResultCode.BAD_REQUEST.getCode(), e.getMessage());
        }
    }

    @PostMapping("/user/verify-code")
    public ApiResponse<AuthDTO.VerifyCodeResponse> verifyCode(
            @RequestBody AuthDTO.VerifyCodeRequest request) {
        try {
            AuthDTO.VerifyCodeResponse response = authService.verifyCode(request);
            if (response.getVerified()) {
                return ApiResponse.success("验证码验证成功", response);
            } else {
                return ApiResponse.error(ResultCode.BAD_REQUEST.getCode(), "验证码错误或已过期");
            }
        } catch (Exception e) {
            return ApiResponse.error(ResultCode.BAD_REQUEST.getCode(), e.getMessage());
        }
    }

    @PostMapping("/register")
    public ApiResponse<AuthDTO.RegisterResponse> register(@RequestBody AuthDTO.RegisterRequest request) {
        try {
            // 参数验证
            if (request == null) {
                return ApiResponse.error(ResultCode.BAD_REQUEST.getCode(), "请求体不能为空");
            }
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ApiResponse.error(ResultCode.BAD_REQUEST.getCode(), "邮箱不能为空");
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return ApiResponse.error(ResultCode.BAD_REQUEST.getCode(), "密码不能为空");
            }
            if (request.getUserType() == null || request.getUserType().trim().isEmpty()) {
                return ApiResponse.error(ResultCode.BAD_REQUEST.getCode(), "用户类型不能为空");
            }
            if (request.getName() == null || request.getName().trim().isEmpty()) {
                return ApiResponse.error(ResultCode.BAD_REQUEST.getCode(), "姓名不能为空");
            }
            if (request.getVerificationToken() == null || request.getVerificationToken().trim().isEmpty()) {
                return ApiResponse.error(ResultCode.BAD_REQUEST.getCode(), "验证token不能为空");
            }
            if (!"job_seeker".equalsIgnoreCase(request.getUserType()) && 
                !"recruiters".equalsIgnoreCase(request.getUserType())) {
                return ApiResponse.error(ResultCode.BAD_REQUEST.getCode(), "用户类型必须是 job_seeker 或 recruiters");
            }
            if ("recruiters".equalsIgnoreCase(request.getUserType()) && 
                (request.getTitle() == null || request.getTitle().trim().isEmpty())) {
                return ApiResponse.error(ResultCode.BAD_REQUEST.getCode(), "招聘者必须提供职位信息");
            }
            
            AuthDTO.RegisterResponse response = authService.register(request);
            return ApiResponse.success("注册成功", response);
        } catch (Exception e) {
            return ApiResponse.error(ResultCode.BAD_REQUEST.getCode(), e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthDTO.RefreshTokenResponse> refreshToken(
            @RequestBody AuthDTO.RefreshTokenRequest request) {
        try {
            AuthDTO.RefreshTokenResponse response = authService.refreshToken(request);
            return ApiResponse.success("Token刷新成功", response);
        } catch (Exception e) {
            return ApiResponse.error(ResultCode.UNAUTHORIZED.getCode(), e.getMessage());
        }
    }

    @PostMapping("/logout")
    @RequireAuth
    public ApiResponse<Void> logout() {
        return ApiResponse.success("退出登录成功", null);
    }
}
