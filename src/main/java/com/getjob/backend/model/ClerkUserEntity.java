package com.getjob.backend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@TableName("clerk_users")
public class ClerkUserEntity {
    @TableId(type = IdType.INPUT)
    private String clerkUserId;

    private String email;

    private String phone;

    private String password;

    private Instant createdAt;

    private Instant updatedAt;
}