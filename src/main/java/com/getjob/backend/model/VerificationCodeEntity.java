package com.getjob.backend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@TableName("verification_codes")
public class VerificationCodeEntity {
    @TableId(type = IdType.AUTO)
    private Long verificationId;

    private String email;

    private String code;

    private String codeType;

    private Boolean isUsed;

    private Instant expiresAt;

    private Instant usedAt;

    private Instant createdAt;
}

