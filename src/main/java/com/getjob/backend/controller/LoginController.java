package com.getjob.backend.controller;

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
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<AuthDTO.LoginResponse> login(@RequestBody AuthDTO.LoginRequest request) {
        try {
            AuthDTO.LoginResponse response = authService.login(request);
            return ApiResponse.success("登录成功", response);
        } catch (Exception e) {
            return ApiResponse.error(ResultCode.UNAUTHORIZED.getCode(), e.getMessage());
        }
    }
}

