package com.getjob.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.getjob.backend.mapper.ResumeMapper;
import com.getjob.backend.mapper.TalentMapper;
import com.getjob.backend.model.ResumeEntity;
import com.getjob.backend.model.TalentEntity;
import com.getjob.backend.service.TalentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TalentServiceImpl implements TalentService {

    private final TalentMapper talentMapper;
    private final ResumeMapper resumeMapper;

    @Override
    public Page<TalentEntity> findTalents(
            String keyword,
            String officeMode,
            String workType,
            BigDecimal salaryMin,
            BigDecimal salaryMax,
            Integer page,
            Integer pageSize) {

        Page<TalentEntity> talentPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<TalentEntity> queryWrapper = new LambdaQueryWrapper<>();

        // 这里需要关联查询简历信息来过滤，暂时简化处理
        // 实际应该使用JOIN查询或子查询
        queryWrapper.orderByDesc(TalentEntity::getUpdatedAt);

        talentMapper.selectPage(talentPage, queryWrapper);

        // 过滤有简历的人才
        List<TalentEntity> filteredTalents = talentPage.getRecords().stream()
                .filter(talent -> {
                    ResumeEntity resume = resumeMapper.selectOne(
                            new LambdaQueryWrapper<ResumeEntity>()
                                    .eq(ResumeEntity::getTalentId, talent.getId())
                    );
                    if (resume == null) {
                        return false;
                    }

                    // 应用过滤条件
                    if (officeMode != null && !officeMode.isEmpty() && !resume.getOfficeMode().equals(officeMode)) {
                        return false;
                    }
                    if (workType != null && !workType.isEmpty() && !resume.getWorkNature().equals(workType)) {
                        return false;
                    }
                    if (salaryMin != null && resume.getMinSalary() != null && resume.getMinSalary().compareTo(salaryMin) < 0) {
                        return false;
                    }
                    if (salaryMax != null && resume.getMaxSalary() != null && resume.getMaxSalary().compareTo(salaryMax) > 0) {
                        return false;
                    }
                    return true;
                })
                .toList();

        // 创建新的分页结果
        Page<TalentEntity> resultPage = new Page<>(page, pageSize);
        resultPage.setRecords(filteredTalents);
        resultPage.setTotal(filteredTalents.size());
        return resultPage;
    }
}