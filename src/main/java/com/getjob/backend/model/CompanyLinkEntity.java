package com.getjob.backend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("company_links")
public class CompanyLinkEntity {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long companyId;

    private String linkType;

    private String url;
}
