package com.getjob.backend.controller;

import com.getjob.backend.annotation.RequireAuth;
import com.getjob.backend.common.ApiResponse;
import com.getjob.backend.common.ResultCode;
import com.getjob.backend.dto.ResumeDTO;
import com.getjob.backend.service.ResumeService;
import com.getjob.backend.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping("/resumes")
    @RequireAuth
    public ApiResponse<ResumeDTO.CreateResumeResponse> createResume(
            @RequestBody ResumeDTO.CreateResumeRequest request) {
        try {
            String userId = UserContext.getUserId();
            ResumeDTO.CreateResumeResponse response = resumeService.createResume(userId, request);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(ResultCode.BAD_REQUEST.getCode(), e.getMessage());
        }
    }

    @GetMapping("/resumes/{resumeId}")
    @RequireAuth
    public ApiResponse<ResumeDTO.ResumeDetailResponse> getResumeDetail(
            @PathVariable Long resumeId) {
        try {
            String userId = UserContext.getUserId();
            ResumeDTO.ResumeDetailResponse response = resumeService.getResumeDetail(userId, resumeId);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(ResultCode.NOT_FOUND.getCode(), e.getMessage());
        }
    }

    @PutMapping("/resumes/{resumeId}")
    @RequireAuth
    public ApiResponse<ResumeDTO.CreateResumeResponse> updateResume(
            @PathVariable Long resumeId,
            @RequestBody ResumeDTO.CreateResumeRequest request) {
        try {
            String userId = UserContext.getUserId();
            ResumeDTO.CreateResumeResponse response = resumeService.updateResume(userId, resumeId, request);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(ResultCode.BAD_REQUEST.getCode(), e.getMessage());
        }
    }

    @DeleteMapping("/resumes/{resumeId}")
    @RequireAuth
    public ApiResponse<Void> deleteResume(@PathVariable Long resumeId) {
        try {
            String userId = UserContext.getUserId();
            resumeService.deleteResume(userId, resumeId);
            return ApiResponse.success("删除成功", null);
        } catch (Exception e) {
            return ApiResponse.error(ResultCode.BAD_REQUEST.getCode(), e.getMessage());
        }
    }
}
