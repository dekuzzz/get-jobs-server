package com.getjob.backend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("resume_skills")
public class ResumeSkillEntity {
    @TableId(type = IdType.AUTO)
    private Long resumeSkillId;

    private Long resumeId;

    private String languageCode;

    private String skillName;

    private String proficiency;
}