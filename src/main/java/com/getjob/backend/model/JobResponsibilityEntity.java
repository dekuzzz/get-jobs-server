package com.getjob.backend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("job_responsibility")
public class JobResponsibilityEntity {
    @TableId(type = IdType.AUTO)
    private Long jobResponsibilityId;

    private Long jobId;

    private String languageCode;

    private String responsibilityText;
}
