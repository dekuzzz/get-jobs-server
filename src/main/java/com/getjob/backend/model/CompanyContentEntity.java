package com.getjob.backend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("company_content")
public class CompanyContentEntity {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long companyId;

    private String languageCode;

    private String slogan;

    private String support;

    private String description;
}
