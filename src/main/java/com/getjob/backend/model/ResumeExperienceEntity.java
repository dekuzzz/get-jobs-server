package com.getjob.backend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@TableName("resume_experiences")
public class ResumeExperienceEntity {
    @TableId(type = IdType.AUTO)
    private Long resumeExperienceId;

    private Long resumeId;

    private String languageCode;

    private String companyName;

    private String position;

    private String workingTime;

    private LocalDate startDate;

    private LocalDate endDate;

    private String description;
}
