package com.getjob.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.getjob.backend.dto.JobDTO;

public interface JobService {
    JobDTO.CreateJobResponse createJob(String userId, JobDTO.CreateJobRequest request);
    JobDTO.JobListResponse getJobList(Integer page, Integer size, String keyword, String category,
                                       String officeMode, String workType, String minSalary, String maxSalary);
    JobDTO.JobDetailResponse getJobDetail(Long jobId);
    JobDTO.CreateJobResponse updateJob(String userId, Long jobId, JobDTO.CreateJobRequest request);
    void deleteJob(String userId, Long jobId);
}
