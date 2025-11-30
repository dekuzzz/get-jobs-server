package com.getjob.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.getjob.backend.model.TalentEntity;

import java.math.BigDecimal;

public interface TalentService {
    Page<TalentEntity> findTalents(
            String keyword,
            String officeMode,
            String workType,
            BigDecimal salaryMin,
            BigDecimal salaryMax,
            Integer page,
            Integer pageSize);
}
