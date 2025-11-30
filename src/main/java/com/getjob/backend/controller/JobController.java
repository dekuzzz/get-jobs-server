package com.getjob.backend.controller;

import com.getjob.backend.annotation.RequireAuth;
import com.getjob.backend.common.ApiResponse;
import com.getjob.backend.common.ResultCode;
import com.getjob.backend.dto.JobDTO;
import com.getjob.backend.service.JobService;
import com.getjob.backend.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping("/job")
    @RequireAuth
    public ApiResponse<JobDTO.CreateJobResponse> createJob(
            @RequestBody JobDTO.CreateJobRequest request) {
        try {
            String userId = UserContext.getUserId();
            JobDTO.CreateJobResponse response = jobService.createJob(userId, request);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(ResultCode.BAD_REQUEST.getCode(), e.getMessage());
        }
    }

    @GetMapping("/job")
    public ApiResponse<JobDTO.JobListResponse> getJobList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "12") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String officeMode,
            @RequestParam(required = false) String workType,
            @RequestParam(required = false) String minSalary,
            @RequestParam(required = false) String maxSalary) {
        try {
            JobDTO.JobListResponse response = jobService.getJobList(
                    page, size, keyword, category, officeMode, workType, minSalary, maxSalary);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(ResultCode.BAD_REQUEST.getCode(), e.getMessage());
        }
    }

    @GetMapping("/jobs/{jobId}")
    public ApiResponse<JobDTO.JobDetailResponse> getJobDetail(@PathVariable Long jobId) {
        try {
            JobDTO.JobDetailResponse response = jobService.getJobDetail(jobId);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(ResultCode.NOT_FOUND.getCode(), e.getMessage());
        }
    }

    @PutMapping("/jobs/{jobId}")
    @RequireAuth
    public ApiResponse<JobDTO.CreateJobResponse> updateJob(
            @PathVariable Long jobId,
            @RequestBody JobDTO.CreateJobRequest request) {
        try {
            String userId = UserContext.getUserId();
            JobDTO.CreateJobResponse response = jobService.updateJob(userId, jobId, request);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(ResultCode.BAD_REQUEST.getCode(), e.getMessage());
        }
    }

    @DeleteMapping("/jobs/{jobId}")
    @RequireAuth
    public ApiResponse<Void> deleteJob(@PathVariable Long jobId) {
        try {
            String userId = UserContext.getUserId();
            jobService.deleteJob(userId, jobId);
            return ApiResponse.success("删除成功", null);
        } catch (Exception e) {
            return ApiResponse.error(ResultCode.BAD_REQUEST.getCode(), e.getMessage());
        }
    }
}
