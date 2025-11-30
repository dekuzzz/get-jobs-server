package com.getjob.backend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@TableName("recruiters")
public class RecruiterEntity {
    @TableId(type = IdType.AUTO)
    private Long recruiterId;

    private Long userId;

    private String fullName;

    private String avatar;

    private String professionalTitle;

    private Boolean isVerified;

    private Boolean isActive;

    private Instant createdAt;

    private Instant updatedAt;
}

