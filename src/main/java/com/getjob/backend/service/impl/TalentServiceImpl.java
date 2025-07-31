package com.getjob.backend.service.impl;

import com.getjob.backend.model.TalentEntity;
import com.getjob.backend.repository.TalentRepository;
import com.getjob.backend.repository.specification.TalentSpecifications;
import com.getjob.backend.service.TalentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TalentServiceImpl implements TalentService {

    private final TalentRepository talentRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<TalentEntity> findTalents(
            String keyword,
            String officeMode,
            String workType,
            Integer salaryMin,
            Integer salaryMax,
            Pageable pageable) {

        Specification<TalentEntity> spec = Specification.allOf(TalentSpecifications.withKeyword(keyword))
                .and(TalentSpecifications.withOfficeMode(officeMode))
                .and(TalentSpecifications.withWorkType(workType))
                .and(TalentSpecifications.withMinSalary(salaryMin))
                .and(TalentSpecifications.withMaxSalary(salaryMax));

        Pageable validPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("createdAt").descending()  // 使用确定存在的字段
        );

        return talentRepository.findAll(spec, validPageable);
    }
}