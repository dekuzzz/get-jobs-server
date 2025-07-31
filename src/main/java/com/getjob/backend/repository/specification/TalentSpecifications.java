package com.getjob.backend.repository.specification;

import com.getjob.backend.model.TalentEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class TalentSpecifications {

    public static Specification<TalentEntity> withKeyword(String keyword) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(keyword)) {
                return null;
            }
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("fullName")), likePattern),
                    cb.like(cb.lower(root.join("resumeEntity").get("position")), likePattern)
            );
        };
    }

    public static Specification<TalentEntity> withOfficeMode(String officeMode) {
        return (root, query, cb) ->
                !StringUtils.hasText(officeMode) ?
                        null :
                        cb.equal(root.join("resumeEntity").get("officeMode"), officeMode);
    }

    public static Specification<TalentEntity> withWorkType(String workType) {
        return (root, query, cb) ->
                !StringUtils.hasText(workType) ?
                        null :
                        cb.equal(root.join("resumeEntity").get("workNature"), workType);
    }

    public static Specification<TalentEntity> withMinSalary(Integer minSalary) {
        return (root, query, cb) ->
                minSalary == null ?
                        null :
                        cb.greaterThanOrEqualTo(root.join("resumeEntity").get("minSalary"), minSalary);
    }

    public static Specification<TalentEntity> withMaxSalary(Integer maxSalary) {
        return (root, query, cb) ->
                maxSalary == null ?
                        null :
                        cb.lessThanOrEqualTo(root.join("resumeEntity").get("maxSalary"), maxSalary);
    }
}