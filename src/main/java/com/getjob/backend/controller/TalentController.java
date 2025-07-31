package com.getjob.backend.controller;

import com.getjob.backend.model.ResumeEntity;
import com.getjob.backend.model.TalentEntity;
import com.getjob.backend.model.dto.TalentListResponse;
import com.getjob.backend.service.TalentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TalentController {

    private final TalentService talentService;

    public TalentController(TalentService talentService) {
        this.talentService = talentService;
    }

    @GetMapping("/talents")
    public ResponseEntity<TalentListResponse> getTalents(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "8") int pageSize,
            @RequestParam(required = true) String language,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String officeMode,
            @RequestParam(required = false) String workType,
            @RequestParam(required = false) Integer salaryMin,
            @RequestParam(required = false) Integer salaryMax) {

        // Create pageable with sorting (optional - you can adjust the default sort)
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "postedDate"));

        // Call service to get filtered talents
        Page<TalentEntity> talentPage = talentService.findTalents(
                keyword, officeMode, workType, salaryMin, salaryMax, pageable);

        // Convert entities to DTOs
        List<TalentListResponse.Talent> talentDTOs = talentPage.getContent().stream()
                .map(talentEntity -> convertToDTO(talentEntity, language))
                .collect(Collectors.toList());

        // Build pagination info
        TalentListResponse.Pagination pagination = new TalentListResponse.Pagination(
                page,
                talentPage.getTotalPages(),
                (int) talentPage.getTotalElements(),
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

        ResumeEntity resumeEntity = entity.getResumeEntity();
        dto.setTitle(resumeEntity.getTitle());
        List<String> tags = new ArrayList<>();
        tags.add(resumeEntity.getOfficeMode());
        tags.add(resumeEntity.getWorkNature());
        dto.setTags(tags);

        List<String> skills = new ArrayList<>();
        for (var skill : resumeEntity.getSkills()) {
            if (skill != null && skill.getLanguageCode().equals(language)) {
                skills.add(skill.getSkillName());
            }
        }
        dto.setSkills(skills);
        dto.setSalary(formatSalary(resumeEntity.getCurrencyCode(), resumeEntity.getMinSalary(), resumeEntity.getMaxSalary(), resumeEntity.getSettlement()));
        dto.setPosted(formatPostedTime(resumeEntity.getUpdatedAt()));

        return dto;
    }

    private String formatSalary(String currency, Double min, Double max, String period) {
        // 默认值处理
        currency = currency != null ? currency.trim() : "";
        period = period != null ? period.trim() : "";
        min = min != null ? min : 0;
        max = max != null ? max : 0;

        // 判断是否需要显示小数位
        boolean showDecimals = (min % 1 != 0) || (max % 1 != 0);

        // 根据是否需要小数选择不同格式
        String formatPattern = showDecimals ? "%,.2f" : "%,.0f";

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