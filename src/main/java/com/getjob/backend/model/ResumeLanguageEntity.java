package com.getjob.backend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("resume_languages")
public class ResumeLanguageEntity {
    @TableId(type = IdType.AUTO)
    private Long resumeLanguageId;

    private Long resumeId;

    private String languageCode;

    private String languageName;

    private String proficiency;
}