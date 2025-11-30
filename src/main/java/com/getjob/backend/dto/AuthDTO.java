package com.getjob.backend.dto;

import lombok.Data;

import java.util.List;

public class AuthDTO {

    @Data
    public static class SendVerificationCodeRequest {
        private String email;
    }

    @Data
    public static class SendVerificationCodeResponse {
        private String email;
        private String purpose;
        private Integer expireIn;
        private Integer retryAfter;
        private String code;
    }

    @Data
    public static class VerifyCodeRequest {
        private String email;
        private String code;
    }

    @Data
    public static class VerifyCodeResponse {
        private Boolean verified;
        private String verificationToken;
    }

    @Data
    public static class RegisterRequest {
        private String email;
        private String password;
        private String userType; // job_seeker | recruiters
        private String name;
        private String verificationToken;
        private String title;
    }

    @Data
    public static class RegisterResponse {
        private Long userId;
        private Long talentId;
        private Long recruiterId;
        private String email;
        private String userType;
        private String name;
    }

    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Data
    public static class UserInfo {
        private Long userId;
        private String email;
        private String userType;
        private String name;
    }

    @Data
    public static class LoginResponse {
        private String accessToken;
        private String refreshToken;
        private String tokenType;
        private Integer expiresIn;
        private UserInfo userInfo;
    }

    @Data
    public static class RefreshTokenRequest {
        private String refreshToken;
    }

    @Data
    public static class RefreshTokenResponse {
        private String accessToken;
        private String refreshToken;
        private String tokenType;
        private Integer expiresIn;
    }
}

