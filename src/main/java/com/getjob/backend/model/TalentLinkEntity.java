package com.getjob.backend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("talents_links")
public class TalentLinkEntity {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long talentId;

    private String linkType;

    private String url;
}
