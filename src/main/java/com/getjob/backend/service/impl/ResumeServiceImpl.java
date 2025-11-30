package com.getjob.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.getjob.backend.dto.ResumeDTO;
import com.getjob.backend.mapper.*;
import com.getjob.backend.model.*;
import com.getjob.backend.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeMapper resumeMapper;
    private final TalentMapper talentMapper;
    private final ResumeSkillMapper resumeSkillMapper;
    private final ResumeLanguageMapper resumeLanguageMapper;
    private final ResumeExperienceMapper resumeExperienceMapper;
    private final ResumeAdvantageMapper resumeAdvantageMapper;
    private final ResumeEducationMapper resumeEducationMapper;

    @Override
    @Transactional
    public ResumeDTO.CreateResumeResponse createResume(String userId, ResumeDTO.CreateResumeRequest request) {
        Long userIdLong = Long.parseLong(userId);
        
        TalentEntity talent = talentMapper.selectOne(
                new LambdaQueryWrapper<TalentEntity>()
                        .eq(TalentEntity::getUserId, userIdLong)
        );

        if (talent == null) {
            throw new RuntimeException("人才信息不存在");
        }

        ResumeEntity resume = new ResumeEntity();
        resume.setTalentId(talent.getId());
        resume.setTitle(request.getTitle());
        resume.setWorkNature(request.getWorkType());
        resume.setOfficeMode(request.getOfficeMode());
        
        if (request.getMinSalary() != null && !request.getMinSalary().isEmpty()) {
            resume.setMinSalary(new BigDecimal(request.getMinSalary()));
        }
        if (request.getMaxSalary() != null && !request.getMaxSalary().isEmpty()) {
            resume.setMaxSalary(new BigDecimal(request.getMaxSalary()));
        }
        
        if (request.getPersonDetail() != null && !request.getPersonDetail().isEmpty()) {
            ResumeDTO.PersonDetail personDetail = request.getPersonDetail().get(0);
            resume.setName(personDetail.getName());
            resume.setPhoneNumber(personDetail.getPhoneNumber());
            resume.setEmailAddress(personDetail.getEmailAddress());
            resume.setTelegram(personDetail.getTelegram());
            resume.setWechat(personDetail.getWechat());
        }
        
        resume.setIsActive(true);
        resume.setCreatedAt(Instant.now());
        resume.setUpdatedAt(Instant.now());
        resumeMapper.insert(resume);

        // 创建技能
        if (request.getSkills() != null) {
            for (String skill : request.getSkills()) {
                ResumeSkillEntity skillEntity = new ResumeSkillEntity();
                skillEntity.setResumeId(resume.getResumeId());
                skillEntity.setLanguageCode("zh");
                skillEntity.setSkillName(skill);
                resumeSkillMapper.insert(skillEntity);
            }
        }

        // 创建语言
        if (request.getLanguage() != null) {
            ResumeLanguageEntity languageEntity = new ResumeLanguageEntity();
            languageEntity.setResumeId(resume.getResumeId());
            languageEntity.setLanguageCode("zh");
            languageEntity.setLanguageName(request.getLanguage());
            resumeLanguageMapper.insert(languageEntity);
        }

        // 创建教育经历
        if (request.getEducation() != null) {
            ResumeEducationEntity educationEntity = new ResumeEducationEntity();
            educationEntity.setResumeId(resume.getResumeId());
            educationEntity.setLanguageCode("zh");
            educationEntity.setDegree(request.getEducation());
            resumeEducationMapper.insert(educationEntity);
        }

        // 创建工作经验
        if (request.getExperience() != null) {
            ResumeExperienceEntity experienceEntity = new ResumeExperienceEntity();
            experienceEntity.setResumeId(resume.getResumeId());
            experienceEntity.setLanguageCode("zh");
            experienceEntity.setDescription(request.getExperience());
            experienceEntity.setCompanyName(""); // 根据实际需求设置
            experienceEntity.setPosition(""); // 根据实际需求设置
            resumeExperienceMapper.insert(experienceEntity);
        }

        // 创建优势
        if (request.getAdvantage() != null) {
            ResumeAdvantageEntity advantageEntity = new ResumeAdvantageEntity();
            advantageEntity.setResumeId(resume.getResumeId());
            advantageEntity.setLanguageCode("zh");
            advantageEntity.setContent(request.getAdvantage());
            resumeAdvantageMapper.insert(advantageEntity);
        }

        ResumeDTO.CreateResumeResponse response = new ResumeDTO.CreateResumeResponse();
        response.setResumeId(resume.getResumeId());
        response.setUpdatedAt(resume.getUpdatedAt().toString());
        return response;
    }

    @Override
    public ResumeDTO.ResumeDetailResponse getResumeDetail(String userId, Long resumeId) {
        ResumeEntity resume = resumeMapper.selectById(resumeId);
        if (resume == null) {
            throw new RuntimeException("简历不存在");
        }

        Long userIdLong = Long.parseLong(userId);
        
        // 验证权限
        TalentEntity talent = talentMapper.selectById(resume.getTalentId());
        if (talent == null || !talent.getUserId().equals(userIdLong)) {
            throw new RuntimeException("无权访问此简历");
        }

        ResumeDTO.ResumeDetailResponse response = new ResumeDTO.ResumeDetailResponse();
        response.setResumeId(resume.getResumeId());
        response.setTitle(resume.getTitle());
        response.setWorkType(resume.getWorkNature());
        response.setOfficeMode(resume.getOfficeMode());
        response.setMinSalary(resume.getMinSalary() != null ? resume.getMinSalary().toString() : null);
        response.setMaxSalary(resume.getMaxSalary() != null ? resume.getMaxSalary().toString() : null);
        response.setCreatedAt(resume.getCreatedAt() != null ? resume.getCreatedAt().toString() : null);
        response.setUpdatedAt(resume.getUpdatedAt() != null ? resume.getUpdatedAt().toString() : null);

        // 设置个人信息
        ResumeDTO.PersonDetail personDetail = new ResumeDTO.PersonDetail();
        personDetail.setName(resume.getName());
        personDetail.setPhoneNumber(resume.getPhoneNumber());
        personDetail.setEmailAddress(resume.getEmailAddress());
        personDetail.setTelegram(resume.getTelegram());
        personDetail.setWechat(resume.getWechat());
        response.setPersonDetail(List.of(personDetail));

        // 获取技能
        List<ResumeSkillEntity> skills = resumeSkillMapper.selectList(
                new LambdaQueryWrapper<ResumeSkillEntity>()
                        .eq(ResumeSkillEntity::getResumeId, resumeId)
        );
        response.setSkills(skills.stream().map(ResumeSkillEntity::getSkillName).collect(Collectors.toList()));

        // 获取语言
        ResumeLanguageEntity language = resumeLanguageMapper.selectOne(
                new LambdaQueryWrapper<ResumeLanguageEntity>()
                        .eq(ResumeLanguageEntity::getResumeId, resumeId)
        );
        if (language != null) {
            response.setLanguage(language.getLanguageName());
        }

        // 获取教育经历
        ResumeEducationEntity education = resumeEducationMapper.selectOne(
                new LambdaQueryWrapper<ResumeEducationEntity>()
                        .eq(ResumeEducationEntity::getResumeId, resumeId)
        );
        if (education != null) {
            response.setEducation(education.getDegree());
        }

        // 获取工作经验
        ResumeExperienceEntity experience = resumeExperienceMapper.selectOne(
                new LambdaQueryWrapper<ResumeExperienceEntity>()
                        .eq(ResumeExperienceEntity::getResumeId, resumeId)
        );
        if (experience != null) {
            response.setExperience(experience.getDescription());
        }

        // 获取优势
        ResumeAdvantageEntity advantage = resumeAdvantageMapper.selectOne(
                new LambdaQueryWrapper<ResumeAdvantageEntity>()
                        .eq(ResumeAdvantageEntity::getResumeId, resumeId)
        );
        if (advantage != null) {
            response.setAdvantage(advantage.getContent());
        }

        return response;
    }

    @Override
    @Transactional
    public ResumeDTO.CreateResumeResponse updateResume(String userId, Long resumeId, ResumeDTO.CreateResumeRequest request) {
        ResumeEntity resume = resumeMapper.selectById(resumeId);
        if (resume == null) {
            throw new RuntimeException("简历不存在");
        }

        Long userIdLong = Long.parseLong(userId);
        
        TalentEntity talent = talentMapper.selectById(resume.getTalentId());
        if (talent == null || !talent.getUserId().equals(userIdLong)) {
            throw new RuntimeException("无权修改此简历");
        }

        resume.setTitle(request.getTitle());
        resume.setWorkNature(request.getWorkType());
        resume.setOfficeMode(request.getOfficeMode());
        
        if (request.getMinSalary() != null && !request.getMinSalary().isEmpty()) {
            resume.setMinSalary(new BigDecimal(request.getMinSalary()));
        }
        if (request.getMaxSalary() != null && !request.getMaxSalary().isEmpty()) {
            resume.setMaxSalary(new BigDecimal(request.getMaxSalary()));
        }
        
        if (request.getPersonDetail() != null && !request.getPersonDetail().isEmpty()) {
            ResumeDTO.PersonDetail personDetail = request.getPersonDetail().get(0);
            resume.setName(personDetail.getName());
            resume.setPhoneNumber(personDetail.getPhoneNumber());
            resume.setEmailAddress(personDetail.getEmailAddress());
            resume.setTelegram(personDetail.getTelegram());
            resume.setWechat(personDetail.getWechat());
        }
        
        resume.setUpdatedAt(Instant.now());
        resumeMapper.updateById(resume);

        // 更新技能（删除旧的，插入新的）
        resumeSkillMapper.delete(
                new LambdaQueryWrapper<ResumeSkillEntity>()
                        .eq(ResumeSkillEntity::getResumeId, resumeId)
        );
        if (request.getSkills() != null) {
            for (String skill : request.getSkills()) {
                ResumeSkillEntity skillEntity = new ResumeSkillEntity();
                skillEntity.setResumeId(resumeId);
                skillEntity.setLanguageCode("zh");
                skillEntity.setSkillName(skill);
                resumeSkillMapper.insert(skillEntity);
            }
        }

        // 更新语言
        resumeLanguageMapper.delete(
                new LambdaQueryWrapper<ResumeLanguageEntity>()
                        .eq(ResumeLanguageEntity::getResumeId, resumeId)
        );
        if (request.getLanguage() != null) {
            ResumeLanguageEntity languageEntity = new ResumeLanguageEntity();
            languageEntity.setResumeId(resumeId);
            languageEntity.setLanguageCode("zh");
            languageEntity.setLanguageName(request.getLanguage());
            resumeLanguageMapper.insert(languageEntity);
        }

        // 更新教育经历
        resumeEducationMapper.delete(
                new LambdaQueryWrapper<ResumeEducationEntity>()
                        .eq(ResumeEducationEntity::getResumeId, resumeId)
        );
        if (request.getEducation() != null) {
            ResumeEducationEntity educationEntity = new ResumeEducationEntity();
            educationEntity.setResumeId(resumeId);
            educationEntity.setLanguageCode("zh");
            educationEntity.setDegree(request.getEducation());
            resumeEducationMapper.insert(educationEntity);
        }

        // 更新工作经验
        resumeExperienceMapper.delete(
                new LambdaQueryWrapper<ResumeExperienceEntity>()
                        .eq(ResumeExperienceEntity::getResumeId, resumeId)
        );
        if (request.getExperience() != null) {
            ResumeExperienceEntity experienceEntity = new ResumeExperienceEntity();
            experienceEntity.setResumeId(resumeId);
            experienceEntity.setLanguageCode("zh");
            experienceEntity.setDescription(request.getExperience());
            experienceEntity.setCompanyName("");
            experienceEntity.setPosition("");
            resumeExperienceMapper.insert(experienceEntity);
        }

        // 更新优势
        resumeAdvantageMapper.delete(
                new LambdaQueryWrapper<ResumeAdvantageEntity>()
                        .eq(ResumeAdvantageEntity::getResumeId, resumeId)
        );
        if (request.getAdvantage() != null) {
            ResumeAdvantageEntity advantageEntity = new ResumeAdvantageEntity();
            advantageEntity.setResumeId(resumeId);
            advantageEntity.setLanguageCode("zh");
            advantageEntity.setContent(request.getAdvantage());
            resumeAdvantageMapper.insert(advantageEntity);
        }

        ResumeDTO.CreateResumeResponse response = new ResumeDTO.CreateResumeResponse();
        response.setResumeId(resume.getResumeId());
        response.setUpdatedAt(resume.getUpdatedAt().toString());
        return response;
    }

    @Override
    @Transactional
    public void deleteResume(String userId, Long resumeId) {
        ResumeEntity resume = resumeMapper.selectById(resumeId);
        if (resume == null) {
            throw new RuntimeException("简历不存在");
        }

        Long userIdLong = Long.parseLong(userId);
        
        TalentEntity talent = talentMapper.selectById(resume.getTalentId());
        if (talent == null || !talent.getUserId().equals(userIdLong)) {
            throw new RuntimeException("无权删除此简历");
        }

        resumeMapper.deleteById(resumeId);
    }
}
