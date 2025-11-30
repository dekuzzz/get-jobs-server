package com.getjob.backend.service;

import com.getjob.backend.dto.ResumeDTO;

public interface ResumeService {
    ResumeDTO.CreateResumeResponse createResume(String userId, ResumeDTO.CreateResumeRequest request);
    ResumeDTO.ResumeDetailResponse getResumeDetail(String userId, Long resumeId);
    ResumeDTO.CreateResumeResponse updateResume(String userId, Long resumeId, ResumeDTO.CreateResumeRequest request);
    void deleteResume(String userId, Long resumeId);
}
