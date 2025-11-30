package com.getjob.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.getjob.backend.dto.CompanyDTO;
import com.getjob.backend.mapper.*;
import com.getjob.backend.model.*;
import com.getjob.backend.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyMapper companyMapper;
    private final CompanyContentMapper companyContentMapper;
    private final CompanyLinkMapper companyLinkMapper;
    private final CompanyRecruiterMapper companyRecruiterMapper;
    private final RecruiterMapper recruiterMapper;
    private final UserAccountMapper userAccountMapper;

    @Override
    public CompanyDTO.CompanyInfoResponse getCompanyInfo(String userId) {
        // 从 userId 获取 recruiterId
        Long userIdLong = Long.parseLong(userId);
        
        RecruiterEntity recruiter = recruiterMapper.selectOne(
                new LambdaQueryWrapper<RecruiterEntity>()
                        .eq(RecruiterEntity::getUserId, userIdLong)
        );

        if (recruiter == null) {
            throw new RuntimeException("招聘者信息不存在");
        }

        // 查找该招聘者所属的公司
        CompanyRecruiterEntity companyRecruiter = companyRecruiterMapper.selectOne(
                new LambdaQueryWrapper<CompanyRecruiterEntity>()
                        .eq(CompanyRecruiterEntity::getRecruiterId, recruiter.getRecruiterId())
                        .eq(CompanyRecruiterEntity::getStatus, "active")
        );

        if (companyRecruiter == null) {
            throw new RuntimeException("公司信息不存在");
        }

        CompanyEntity company = companyMapper.selectById(companyRecruiter.getCompanyId());

        if (company == null) {
            throw new RuntimeException("公司信息不存在");
        }

        CompanyContentEntity content = companyContentMapper.selectOne(
                new LambdaQueryWrapper<CompanyContentEntity>()
                        .eq(CompanyContentEntity::getCompanyId, company.getCompanyId())
                        .eq(CompanyContentEntity::getLanguageCode, company.getLanguageCode() != null ? company.getLanguageCode() : "zh")
        );

        List<CompanyLinkEntity> links = companyLinkMapper.selectList(
                new LambdaQueryWrapper<CompanyLinkEntity>()
                        .eq(CompanyLinkEntity::getCompanyId, company.getCompanyId())
        );

        CompanyDTO.CompanyInfoResponse response = new CompanyDTO.CompanyInfoResponse();
        response.setCompanyName(company.getFullName());
        response.setAvatar(company.getAvatar());
        response.setLocation(company.getLocation());
        response.setEmail(company.getEmailAddress());
        response.setCompanyWebsite(company.getOfficialWebsite());
        
        if (content != null) {
            response.setSlogan(content.getSlogan());
            response.setIntroduction(content.getDescription());
        }

        // 设置链接
        for (CompanyLinkEntity link : links) {
            switch (link.getLinkType()) {
                case "x":
                    response.setTwitterLink(link.getUrl());
                    break;
                case "github":
                    response.setGithubLink(link.getUrl());
                    break;
            }
        }

        // 解析categoryTags和size
        if (company.getCategoryTags() != null) {
            List<String> categories = List.of(company.getCategoryTags().split(","));
            response.setProjectCategory(categories);
            response.setSupportedChains(categories);
        }

        if (company.getSize() != null) {
            try {
                response.setStaffsNumber(Integer.parseInt(company.getSize()));
            } catch (NumberFormatException e) {
                // ignore
            }
        }

        return response;
    }

    @Override
    @Transactional
    public CompanyDTO.CompanyInfoResponse updateCompanyInfo(String userId, CompanyDTO.UpdateCompanyRequest request) {
        Long userIdLong = Long.parseLong(userId);
        
        RecruiterEntity recruiter = recruiterMapper.selectOne(
                new LambdaQueryWrapper<RecruiterEntity>()
                        .eq(RecruiterEntity::getUserId, userIdLong)
        );

        if (recruiter == null) {
            throw new RuntimeException("招聘者信息不存在");
        }

        CompanyRecruiterEntity companyRecruiter = companyRecruiterMapper.selectOne(
                new LambdaQueryWrapper<CompanyRecruiterEntity>()
                        .eq(CompanyRecruiterEntity::getRecruiterId, recruiter.getRecruiterId())
                        .eq(CompanyRecruiterEntity::getStatus, "active")
        );

        if (companyRecruiter == null) {
            throw new RuntimeException("公司信息不存在");
        }

        CompanyEntity company = companyMapper.selectById(companyRecruiter.getCompanyId());

        if (company == null) {
            throw new RuntimeException("公司信息不存在");
        }

        company.setLocation(request.getLocation());
        company.setEmailAddress(request.getEmail());
        company.setUpdatedAt(Instant.now());
        companyMapper.updateById(company);

        CompanyContentEntity content = companyContentMapper.selectOne(
                new LambdaQueryWrapper<CompanyContentEntity>()
                        .eq(CompanyContentEntity::getCompanyId, company.getCompanyId())
                        .eq(CompanyContentEntity::getLanguageCode, company.getLanguageCode() != null ? company.getLanguageCode() : "zh")
        );

        if (content == null) {
            content = new CompanyContentEntity();
            content.setCompanyId(company.getCompanyId());
            content.setLanguageCode(company.getLanguageCode() != null ? company.getLanguageCode() : "zh");
            companyContentMapper.insert(content);
        }

        content.setDescription(request.getIntroduction());
        companyContentMapper.updateById(content);

        // 更新链接
        updateCompanyLinks(company.getCompanyId(), request);

        return getCompanyInfo(userId);
    }

    private void updateCompanyLinks(Long companyId, CompanyDTO.UpdateCompanyRequest request) {
        // 删除现有链接
        companyLinkMapper.delete(
                new LambdaQueryWrapper<CompanyLinkEntity>()
                        .eq(CompanyLinkEntity::getCompanyId, companyId)
        );

        // 插入新链接
        if (request.getTwitterLink() != null) {
            CompanyLinkEntity link = new CompanyLinkEntity();
            link.setCompanyId(companyId);
            link.setLinkType("x");
            link.setUrl(request.getTwitterLink());
            companyLinkMapper.insert(link);
        }

        if (request.getGithubLink() != null) {
            CompanyLinkEntity link = new CompanyLinkEntity();
            link.setCompanyId(companyId);
            link.setLinkType("github");
            link.setUrl(request.getGithubLink());
            companyLinkMapper.insert(link);
        }

        if (request.getQqLink() != null) {
            CompanyLinkEntity link = new CompanyLinkEntity();
            link.setCompanyId(companyId);
            link.setLinkType("telegram");
            link.setUrl(request.getQqLink());
            companyLinkMapper.insert(link);
        }
    }

    @Override
    @Transactional
    public CompanyDTO.CompanyInfoResponse createCompanyInfo(String userId, CompanyDTO.CreateCompanyRequest request) {
        Long userIdLong = Long.parseLong(userId);
        
        RecruiterEntity recruiter = recruiterMapper.selectOne(
                new LambdaQueryWrapper<RecruiterEntity>()
                        .eq(RecruiterEntity::getUserId, userIdLong)
        );

        if (recruiter == null) {
            throw new RuntimeException("招聘者信息不存在，请先注册为招聘者");
        }

        // 创建公司
        CompanyEntity company = new CompanyEntity();
        company.setOwnerRecruiterId(recruiter.getRecruiterId());
        company.setFullName(request.getCompanyName());
        company.setAvatar(request.getAvatar());
        company.setOfficialWebsite(request.getCompanyWebsite());
        company.setLocation(request.getLocation());
        company.setLanguageCode("zh");
        company.setEmailAddress(request.getEmail());
        company.setSize(request.getStaffsNumber() != null ? String.valueOf(request.getStaffsNumber()) : null);
        company.setCategoryTags(request.getProjectCategory() != null ? 
                String.join(",", request.getProjectCategory()) : null);
        company.setVote(0.0);
        company.setIsActive(true);
        company.setCreatedAt(Instant.now());
        company.setUpdatedAt(Instant.now());
        companyMapper.insert(company);

        // 创建公司招聘者关联（owner角色）
        CompanyRecruiterEntity companyRecruiter = new CompanyRecruiterEntity();
        companyRecruiter.setCompanyId(company.getCompanyId());
        companyRecruiter.setRecruiterId(recruiter.getRecruiterId());
        companyRecruiter.setRole("owner");
        companyRecruiter.setStatus("active");
        companyRecruiter.setJoinedAt(Instant.now());
        companyRecruiter.setCreatedAt(Instant.now());
        companyRecruiter.setUpdatedAt(Instant.now());
        companyRecruiterMapper.insert(companyRecruiter);

        // 创建内容
        CompanyContentEntity content = new CompanyContentEntity();
        content.setCompanyId(company.getCompanyId());
        content.setLanguageCode("zh");
        content.setSlogan(request.getSlogan());
        content.setDescription(request.getIntroduction());
        companyContentMapper.insert(content);

        // 创建链接
        createCompanyLinks(company.getCompanyId(), request);

        return getCompanyInfo(userId);
    }

    private void createCompanyLinks(Long companyId, CompanyDTO.CreateCompanyRequest request) {
        if (request.getTwitterLink() != null) {
            CompanyLinkEntity link = new CompanyLinkEntity();
            link.setCompanyId(companyId);
            link.setLinkType("x");
            link.setUrl(request.getTwitterLink());
            companyLinkMapper.insert(link);
        }

        if (request.getGithubLink() != null) {
            CompanyLinkEntity link = new CompanyLinkEntity();
            link.setCompanyId(companyId);
            link.setLinkType("github");
            link.setUrl(request.getGithubLink());
            companyLinkMapper.insert(link);
        }

        if (request.getTelegramLink() != null) {
            CompanyLinkEntity link = new CompanyLinkEntity();
            link.setCompanyId(companyId);
            link.setLinkType("telegram");
            link.setUrl(request.getTelegramLink());
            companyLinkMapper.insert(link);
        }
    }
}
