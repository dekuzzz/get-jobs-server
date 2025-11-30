package com.getjob.backend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@TableName("companies")
public class CompanyEntity {
    @TableId(type = IdType.AUTO)
    private Long companyId;

    private Long ownerRecruiterId;

    private String fullName;

    private String avatar;

    private String officialWebsite;

    private String location;

    private String languageCode;

    private String size;

    private String categoryTags;

    private String emailAddress;

    private Double vote;

    private Boolean isActive;

    private Instant createdAt;

    private Instant updatedAt;
}
