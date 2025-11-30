package com.getjob.backend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@TableName("company_recruiters")
public class CompanyRecruiterEntity {
    @TableId(type = IdType.AUTO)
    private Long companyRecruiterId;

    private Long companyId;

    private Long recruiterId;

    private String role;

    private String status;

    private Long invitedBy;

    private Instant joinedAt;

    private Instant createdAt;

    private Instant updatedAt;
}

