package com.getjob.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.getjob.backend.mapper.ResumeMapper;
import com.getjob.backend.mapper.ResumeSkillMapper;
import com.getjob.backend.model.ResumeEntity;
import com.getjob.backend.model.ResumeSkillEntity;
import com.getjob.backend.model.TalentEntity;
import com.getjob.backend.model.dto.TalentListResponse;
import com.getjob.backend.service.TalentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TalentController {

    private final TalentService talentService;
    private final ResumeMapper resumeMapper;
    private final ResumeSkillMapper resumeSkillMapper;

    @GetMapping("/talents")
    public ResponseEntity<TalentListResponse> getTalents(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "8") int pageSize,
            @RequestParam String language,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String officeMode,
            @RequestParam(required = false) String workType,
            @RequestParam(required = false) BigDecimal salaryMin,
            @RequestParam(required = false) BigDecimal salaryMax) {

        // Call service to get filtered talents
        Page<TalentEntity> talentPage = talentService.findTalents(
                keyword, officeMode, workType, salaryMin, salaryMax, page, pageSize);

        // Convert entities to DTOs
        List<TalentListResponse.Talent> talentDTOs = talentPage.getRecords().stream()
                .map(talentEntity -> convertToDTO(talentEntity, language))
                .collect(Collectors.toList());

        // Build pagination info
        TalentListResponse.Pagination pagination = new TalentListResponse.Pagination(
                page,
                (int) talentPage.getPages(),
                (int) talentPage.getTotal(),
                pageSize);

        // Build response
        TalentListResponse response = new TalentListResponse();
        response.setSuccess(true);
        response.setData(new TalentListResponse.Data(talentDTOs, pagination));

        return ResponseEntity.ok(response);
    }

    private TalentListResponse.Talent convertToDTO(TalentEntity entity, String language) {
        TalentListResponse.Talent dto = new TalentListResponse.Talent();
        dto.setId(entity.getId());

        TalentListResponse.Recruiter recruiter = new TalentListResponse.Recruiter();
        recruiter.setName(entity.getFullName());
        recruiter.setAvatar(entity.getAvatar());
        dto.setRecruiter(recruiter);

        ResumeEntity resumeEntity = resumeMapper.selectOne(
                new LambdaQueryWrapper<ResumeEntity>()
                        .eq(ResumeEntity::getTalentId, entity.getId())
        );

        if (resumeEntity != null) {
            dto.setTitle(resumeEntity.getTitle());
            List<String> tags = new ArrayList<>();
            if (resumeEntity.getOfficeMode() != null) {
                tags.add(resumeEntity.getOfficeMode());
            }
            if (resumeEntity.getWorkNature() != null) {
                tags.add(resumeEntity.getWorkNature());
            }
            dto.setTags(tags);

            List<ResumeSkillEntity> skills = resumeSkillMapper.selectList(
                    new LambdaQueryWrapper<ResumeSkillEntity>()
                            .eq(ResumeSkillEntity::getResumeId, resumeEntity.getResumeId())
                            .eq(ResumeSkillEntity::getLanguageCode, language)
            );
            dto.setSkills(skills.stream().map(ResumeSkillEntity::getSkillName).collect(Collectors.toList()));
            dto.setSalary(formatSalary(resumeEntity.getCurrencyCode(), resumeEntity.getMinSalary(), resumeEntity.getMaxSalary(), resumeEntity.getSettlement()));
            dto.setPosted(formatPostedTime(resumeEntity.getUpdatedAt()));
        }

        return dto;
    }

    private String formatSalary(String currency, BigDecimal min, BigDecimal max, String period) {
        // 默认值处理
        currency = currency != null ? currency.trim() : "";
        period = period != null ? period.trim() : "";
        min = min != null ? min : BigDecimal.ZERO;
        max = max != null ? max : BigDecimal.ZERO;

        // 根据是否需要小数选择不同格式
        String formatPattern = "%,.2f";

        return String.format("%s%s-%s per %s",
                currency,
                String.format(formatPattern, min),
                String.format(formatPattern, max),
                period);
    }

    private String formatPostedTime(Instant postedDate) {
        if (postedDate == null) {
            return "just now posted";
        }

        Instant now = Instant.now();
        Duration duration = Duration.between(postedDate, now);

        long seconds = duration.getSeconds();

        // 计算各个时间单位
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long weeks = days / 7;
        long months = days / 30;
        long years = days / 365;

        if (years > 0) {
            return years == 1 ? "1 year ago posted" : years + " years ago posted";
        } else if (months > 0) {
            return months == 1 ? "1 month ago posted" : months + " months ago posted";
        } else if (weeks > 0) {
            return weeks == 1 ? "1 week ago posted" : weeks + " weeks ago posted";
        } else if (days > 0) {
            return days == 1 ? "1 day ago posted" : days + " days ago posted";
        } else if (hours > 0) {
            return hours == 1 ? "1 hour ago posted" : hours + " hours ago posted";
        } else if (minutes > 0) {
            return minutes == 1 ? "1 minute ago posted" : minutes + " minutes ago posted";
        } else {
            return seconds <= 5 ? "just now posted" : seconds + " seconds ago posted";
        }
    }
}