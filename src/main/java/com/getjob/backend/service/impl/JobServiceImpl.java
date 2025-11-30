package com.getjob.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.getjob.backend.dto.JobDTO;
import com.getjob.backend.mapper.*;
import com.getjob.backend.model.*;
import com.getjob.backend.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobMapper jobMapper;
    private final CompanyMapper companyMapper;
    private final JobContentMapper jobContentMapper;
    private final JobResponsibilityMapper jobResponsibilityMapper;
    private final JobQualificationMapper jobQualificationMapper;
    private final RecruiterMapper recruiterMapper;
    private final CompanyRecruiterMapper companyRecruiterMapper;

    @Override
    @Transactional
    public JobDTO.CreateJobResponse createJob(String userId, JobDTO.CreateJobRequest request) {
        Long userIdLong = Long.parseLong(userId);
        
        // 获取招聘者信息
        RecruiterEntity recruiter = recruiterMapper.selectOne(
                new LambdaQueryWrapper<RecruiterEntity>()
                        .eq(RecruiterEntity::getUserId, userIdLong)
        );

        if (recruiter == null) {
            throw new RuntimeException("招聘者信息不存在");
        }

        // 获取招聘者所属的公司
        CompanyRecruiterEntity companyRecruiter = companyRecruiterMapper.selectOne(
                new LambdaQueryWrapper<CompanyRecruiterEntity>()
                        .eq(CompanyRecruiterEntity::getRecruiterId, recruiter.getRecruiterId())
                        .eq(CompanyRecruiterEntity::getStatus, "active")
        );

        if (companyRecruiter == null) {
            throw new RuntimeException("您还未加入任何公司");
        }

        JobEntity job = new JobEntity();
        job.setCompanyId(companyRecruiter.getCompanyId());
        job.setCreatedBy(recruiter.getRecruiterId());
        job.setLanguageCode("zh");
        job.setJobName(request.getTitle());
        job.setOfficeMode(request.getOfficeMode());
        job.setWorkNature(request.getWorkType());
        job.setLocation(request.getWorkLocation());
        
        if (request.getMinSalary() != null && !request.getMinSalary().isEmpty()) {
            job.setMinSalary(new BigDecimal(request.getMinSalary()));
        }
        if (request.getMaxSalary() != null && !request.getMaxSalary().isEmpty()) {
            job.setMaxSalary(new BigDecimal(request.getMaxSalary()));
        }
        
        job.setIsActive(true);
        job.setCreatedAt(Instant.now());
        job.setUpdatedAt(Instant.now());
        jobMapper.insert(job);

        // 创建内容
        if (request.getOverview() != null || request.getDetails() != null) {
            JobContentEntity content = new JobContentEntity();
            content.setJobId(job.getJobId());
            content.setLanguageCode("zh");
            content.setContentText((request.getOverview() != null ? request.getOverview() : "") + 
                    "\n" + (request.getDetails() != null ? request.getDetails() : ""));
            jobContentMapper.insert(content);
        }

        // 创建职责
        if (request.getResponsibility() != null && !request.getResponsibility().isEmpty()) {
            JobResponsibilityEntity responsibility = new JobResponsibilityEntity();
            responsibility.setJobId(job.getJobId());
            responsibility.setLanguageCode("zh");
            responsibility.setResponsibilityText(request.getResponsibility());
            jobResponsibilityMapper.insert(responsibility);
        }

        // 创建要求
        if (request.getQualification() != null && !request.getQualification().isEmpty()) {
            JobQualificationEntity qualification = new JobQualificationEntity();
            qualification.setJobId(job.getJobId());
            qualification.setLanguageCode("zh");
            qualification.setQualificationText(request.getQualification());
            jobQualificationMapper.insert(qualification);
        }

        JobDTO.CreateJobResponse response = new JobDTO.CreateJobResponse();
        response.setJobId(job.getJobId());
        response.setTitle(job.getJobName());
        response.setStatus("pending");
        response.setCreatedAt(job.getCreatedAt().toString());
        return response;
    }

    @Override
    public JobDTO.JobListResponse getJobList(Integer page, Integer size, String keyword, String category,
                                              String officeMode, String workType, String minSalary, String maxSalary) {
        Page<JobEntity> jobPage = new Page<>(page, size);
        LambdaQueryWrapper<JobEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(JobEntity::getIsActive, true);

        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.like(JobEntity::getJobName, keyword);
        }
        if (officeMode != null && !officeMode.isEmpty()) {
            queryWrapper.eq(JobEntity::getOfficeMode, officeMode);
        }
        if (workType != null && !workType.isEmpty()) {
            queryWrapper.eq(JobEntity::getWorkNature, workType);
        }
        if (minSalary != null && !minSalary.isEmpty()) {
            queryWrapper.ge(JobEntity::getMinSalary, new BigDecimal(minSalary));
        }
        if (maxSalary != null && !maxSalary.isEmpty()) {
            queryWrapper.le(JobEntity::getMaxSalary, new BigDecimal(maxSalary));
        }

        queryWrapper.orderByDesc(JobEntity::getCreatedAt);
        jobMapper.selectPage(jobPage, queryWrapper);

        List<JobDTO.JobListItem> items = jobPage.getRecords().stream().map(job -> {
            JobDTO.JobListItem item = new JobDTO.JobListItem();
            item.setJobId(job.getJobId());
            item.setCompanyId(job.getCompanyId());
            
            CompanyEntity company = companyMapper.selectById(job.getCompanyId());
            if (company != null) {
                item.setCompanyName(company.getFullName());
            }
            
            item.setWorkType(job.getWorkNature());
            item.setOfficeMode(job.getOfficeMode());
            item.setMinSalary(job.getMinSalary() != null ? job.getMinSalary().toString() : null);
            item.setMaxSalary(job.getMaxSalary() != null ? job.getMaxSalary().toString() : null);
            item.setPriority(200); // 默认优先级
            return item;
        }).collect(Collectors.toList());

        JobDTO.JobListResponse response = new JobDTO.JobListResponse();
        response.setJobs(items);
        
        JobDTO.Pagination pagination = new JobDTO.Pagination();
        pagination.setPage((int) jobPage.getCurrent());
        pagination.setSize((int) jobPage.getSize());
        pagination.setTotal((int) jobPage.getTotal());
        response.setPagination(pagination);

        return response;
    }

    @Override
    public JobDTO.JobDetailResponse getJobDetail(Long jobId) {
        JobEntity job = jobMapper.selectById(jobId);
        if (job == null) {
            throw new RuntimeException("职位不存在");
        }

        JobContentEntity content = jobContentMapper.selectOne(
                new LambdaQueryWrapper<JobContentEntity>()
                        .eq(JobContentEntity::getJobId, jobId)
                        .eq(JobContentEntity::getLanguageCode, "zh")
        );

        JobResponsibilityEntity responsibility = jobResponsibilityMapper.selectOne(
                new LambdaQueryWrapper<JobResponsibilityEntity>()
                        .eq(JobResponsibilityEntity::getJobId, jobId)
                        .eq(JobResponsibilityEntity::getLanguageCode, "zh")
        );

        JobQualificationEntity qualification = jobQualificationMapper.selectOne(
                new LambdaQueryWrapper<JobQualificationEntity>()
                        .eq(JobQualificationEntity::getJobId, jobId)
                        .eq(JobQualificationEntity::getLanguageCode, "zh")
        );

        JobDTO.JobDetailResponse response = new JobDTO.JobDetailResponse();
        response.setJobId(job.getJobId());
        response.setTitle(job.getJobName());
        response.setWorkLocation(job.getLocation());
        response.setWorkType(job.getWorkNature());
        response.setOfficeMode(job.getOfficeMode());
        response.setMinSalary(job.getMinSalary() != null ? job.getMinSalary().toString() : null);
        response.setMaxSalary(job.getMaxSalary() != null ? job.getMaxSalary().toString() : null);
        
        if (content != null) {
            String[] parts = content.getContentText().split("\n", 2);
            response.setOverview(parts.length > 0 ? parts[0] : "");
            response.setDetails(parts.length > 1 ? parts[1] : "");
        }
        
        response.setResponsibility(responsibility != null ? responsibility.getResponsibilityText() : null);
        response.setQualification(qualification != null ? qualification.getQualificationText() : null);
        response.setCreatedAt(job.getCreatedAt() != null ? job.getCreatedAt().toString() : null);

        return response;
    }

    @Override
    @Transactional
    public JobDTO.CreateJobResponse updateJob(String userId, Long jobId, JobDTO.CreateJobRequest request) {
        JobEntity job = jobMapper.selectById(jobId);
        if (job == null) {
            throw new RuntimeException("职位不存在");
        }

        Long userIdLong = Long.parseLong(userId);
        
        // 验证是否是职位创建者
        RecruiterEntity recruiter = recruiterMapper.selectOne(
                new LambdaQueryWrapper<RecruiterEntity>()
                        .eq(RecruiterEntity::getUserId, userIdLong)
        );

        if (recruiter == null || !job.getCreatedBy().equals(recruiter.getRecruiterId())) {
            throw new RuntimeException("无权修改此职位");
        }

        job.setJobName(request.getTitle());
        job.setOfficeMode(request.getOfficeMode());
        job.setWorkNature(request.getWorkType());
        job.setLocation(request.getWorkLocation());
        
        if (request.getMinSalary() != null && !request.getMinSalary().isEmpty()) {
            job.setMinSalary(new BigDecimal(request.getMinSalary()));
        }
        if (request.getMaxSalary() != null && !request.getMaxSalary().isEmpty()) {
            job.setMaxSalary(new BigDecimal(request.getMaxSalary()));
        }
        
        job.setUpdatedAt(Instant.now());
        jobMapper.updateById(job);

        // 更新内容
        JobContentEntity content = jobContentMapper.selectOne(
                new LambdaQueryWrapper<JobContentEntity>()
                        .eq(JobContentEntity::getJobId, jobId)
                        .eq(JobContentEntity::getLanguageCode, "zh")
        );
        if (content != null) {
            content.setContentText((request.getOverview() != null ? request.getOverview() : "") + 
                    "\n" + (request.getDetails() != null ? request.getDetails() : ""));
            jobContentMapper.updateById(content);
        }

        // 更新职责
        JobResponsibilityEntity responsibility = jobResponsibilityMapper.selectOne(
                new LambdaQueryWrapper<JobResponsibilityEntity>()
                        .eq(JobResponsibilityEntity::getJobId, jobId)
                        .eq(JobResponsibilityEntity::getLanguageCode, "zh")
        );
        if (responsibility != null && request.getResponsibility() != null) {
            responsibility.setResponsibilityText(request.getResponsibility());
            jobResponsibilityMapper.updateById(responsibility);
        }

        // 更新要求
        JobQualificationEntity qualification = jobQualificationMapper.selectOne(
                new LambdaQueryWrapper<JobQualificationEntity>()
                        .eq(JobQualificationEntity::getJobId, jobId)
                        .eq(JobQualificationEntity::getLanguageCode, "zh")
        );
        if (qualification != null && request.getQualification() != null) {
            qualification.setQualificationText(request.getQualification());
            jobQualificationMapper.updateById(qualification);
        }

        JobDTO.CreateJobResponse response = new JobDTO.CreateJobResponse();
        response.setJobId(job.getJobId());
        response.setTitle(job.getJobName());
        response.setStatus("pending");
        response.setCreatedAt(job.getCreatedAt().toString());
        return response;
    }

    @Override
    @Transactional
    public void deleteJob(String userId, Long jobId) {
        JobEntity job = jobMapper.selectById(jobId);
        if (job == null) {
            throw new RuntimeException("职位不存在");
        }

        Long userIdLong = Long.parseLong(userId);
        
        // 验证是否是职位创建者
        RecruiterEntity recruiter = recruiterMapper.selectOne(
                new LambdaQueryWrapper<RecruiterEntity>()
                        .eq(RecruiterEntity::getUserId, userIdLong)
        );

        if (recruiter == null || !job.getCreatedBy().equals(recruiter.getRecruiterId())) {
            throw new RuntimeException("无权删除此职位");
        }

        jobMapper.deleteById(jobId);
    }
}
