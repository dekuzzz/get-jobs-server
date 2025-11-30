package com.getjob.backend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@TableName("resumes")
public class ResumeEntity {
    @TableId(type = IdType.AUTO)
    private Long resumeId;

    private Long talentId;

    private String name;

    private String title;

    private String phoneNumber;

    private String emailAddress;

    private String telegram;

    private String wechat;

    private String officeMode;

    private String workNature;

    private String permission;

    private String position;

    private BigDecimal minSalary;

    private BigDecimal maxSalary;

    private String currencyCode;

    private String settlement;

    private Boolean isActive;

    private Instant createdAt;

    private Instant updatedAt;
}
