package com.getjob.backend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@TableName("talents")
public class TalentEntity {
    @TableId(type = IdType.AUTO, value = "talent_id")
    private Long id;

    private Long userId;

    private String fullName;

    private String avatar;

    private String location;

    private Double vote;

    private Boolean isActive;

    private Instant createdAt;

    private Instant updatedAt;
}
