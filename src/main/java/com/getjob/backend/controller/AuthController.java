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

    @PostMapping("/user/register")
    public ApiResponse<AuthDTO.RegisterResponse> userRegister(@RequestBody AuthDTO.RegisterRequest request) {
        try {
            AuthDTO.RegisterResponse response = authService.userRegister(request);
            return ApiResponse.success("注册成功", response);
        } catch (Exception e) {
            return ApiResponse.error(ResultCode.BAD_REQUEST.getCode(), e.getMessage());
        }
    }

    @PostMapping("/register")
    public ApiResponse<AuthDTO.RegisterResponse> register(@RequestBody AuthDTO.RegisterRequest request) {
        try {
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
