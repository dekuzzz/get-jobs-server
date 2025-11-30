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
@TableName("jobs")
public class JobEntity {
    @TableId(type = IdType.AUTO)
    private Long jobId;

    private Long companyId;

    private Long createdBy;

    private String languageCode;

    private String jobName;

    private String officeMode;

    private String workNature;

    private String location;

    private BigDecimal minSalary;

    private BigDecimal maxSalary;

    private String currencyCode;

    private String settlement;

    private Boolean isActive;

    private Instant createdAt;

    private Instant updatedAt;
}
