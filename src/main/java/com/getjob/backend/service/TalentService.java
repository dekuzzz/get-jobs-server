package com.getjob.backend.service;

import com.getjob.backend.model.TalentEntity;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public interface TalentService {

    Page<TalentEntity> findTalents(
            String keyword,
            String officeMode,
            String workType,
            Integer salaryMin,
            Integer salaryMax,
            Pageable pageable);
}
