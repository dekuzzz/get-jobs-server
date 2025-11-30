package com.getjob.backend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("resume_advantages")
public class ResumeAdvantageEntity {
    @TableId(type = IdType.AUTO)
    private Long resumeAdvantageId;

    private Long resumeId;

    private String languageCode;

    private String content;

    private String description;
}