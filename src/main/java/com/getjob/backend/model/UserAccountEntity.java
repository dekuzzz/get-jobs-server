package com.getjob.backend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@TableName("user_accounts")
public class UserAccountEntity {
    @TableId(type = IdType.AUTO)
    private Long userId;

    private String email;

    private String passwordHash;

    private String salt;

    private Boolean isActive;

    private Instant lastLoginAt;

    private Integer loginAttempts;

    private Instant lockedUntil;

    private Instant createdAt;

    private Instant updatedAt;
}

