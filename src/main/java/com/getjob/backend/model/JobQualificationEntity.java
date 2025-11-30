package com.getjob.backend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("job_qualification")
public class JobQualificationEntity {
    @TableId(type = IdType.AUTO)
    private Long jobQualificationId;

    private Long jobId;

    private String languageCode;

    private String qualificationText;
}
