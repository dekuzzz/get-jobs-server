package com.getjob.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.getjob.backend.dto.AuthDTO;
import com.getjob.backend.mapper.RecruiterMapper;
import com.getjob.backend.mapper.TalentMapper;
import com.getjob.backend.mapper.UserAccountMapper;
import com.getjob.backend.mapper.VerificationCodeMapper;
import com.getjob.backend.model.RecruiterEntity;
import com.getjob.backend.model.TalentEntity;
import com.getjob.backend.model.UserAccountEntity;
import com.getjob.backend.model.VerificationCodeEntity;
import com.getjob.backend.util.JwtUtil;
import com.getjob.backend.util.PasswordUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserAccountMapper userAccountMapper;
    private final VerificationCodeMapper verificationCodeMapper;
    private final TalentMapper talentMapper;
    private final RecruiterMapper recruiterMapper;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;
    private final JavaMailSender mailSender;
    // 临时存储验证码（实际应该使用 Redis）
    private final ConcurrentHashMap<String, VerificationCodeEntity> codeStorage = new ConcurrentHashMap<>();
    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * 发送验证码
     */
    public AuthDTO.SendVerificationCodeResponse sendVerificationCode(AuthDTO.SendVerificationCodeRequest request) {
        // 生成4位验证码
        String code = String.format("%04d", new Random().nextInt(10000));

        // 存储验证码到数据库
        VerificationCodeEntity entity = new VerificationCodeEntity();
        entity.setEmail(request.getEmail());
        entity.setCode(code);
        entity.setCodeType("register");
        entity.setIsUsed(false);
        entity.setExpiresAt(Instant.now().plusSeconds(300)); // 5分钟过期
        entity.setCreatedAt(Instant.now());
        verificationCodeMapper.insert(entity);

        // 发送邮件
        try {
            sendVerificationEmail(request.getEmail(), code);
            log.info("验证码邮件发送成功: {}", request.getEmail());
        } catch (Exception e) {
            log.error("验证码邮件发送失败: {}", request.getEmail(), e);
            throw new RuntimeException("邮件发送失败，请稍后重试");
        }

        AuthDTO.SendVerificationCodeResponse response = new AuthDTO.SendVerificationCodeResponse();
        response.setEmail(request.getEmail());
        response.setPurpose("register");
        response.setExpireIn(300);
        response.setRetryAfter(60);
        // response.setCode(code); // 生产环境不应该返回验证码
        return response;
    }
    
    /**
     * 发送验证码邮件
     */
    private void sendVerificationEmail(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("GetJob - 验证码");
        message.setText(String.format(
            "您好！\n\n" +
            "您的验证码是：%s\n\n" +
            "此验证码将在5分钟后过期，请尽快使用。\n" +
            "如果这不是您本人的操作，请忽略此邮件。\n\n" +
            "祝您使用愉快！\n" +
            "GetJob团队",
            code
        ));
        
        mailSender.send(message);
    }

    /**
     * 验证验证码
     */
    public AuthDTO.VerifyCodeResponse verifyCode(AuthDTO.VerifyCodeRequest request) {
        LambdaQueryWrapper<VerificationCodeEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VerificationCodeEntity::getEmail, request.getEmail())
                .eq(VerificationCodeEntity::getCode, request.getCode())
                .eq(VerificationCodeEntity::getIsUsed, false)
                .gt(VerificationCodeEntity::getExpiresAt, Instant.now())
                .orderByDesc(VerificationCodeEntity::getCreatedAt)
                .last("LIMIT 1");

        VerificationCodeEntity entity = verificationCodeMapper.selectOne(wrapper);

        AuthDTO.VerifyCodeResponse response = new AuthDTO.VerifyCodeResponse();
        if (entity != null) {
            // 标记验证码为已使用
            entity.setIsUsed(true);
            entity.setUsedAt(Instant.now());
            verificationCodeMapper.updateById(entity);

            // 生成临时验证token
            String verificationToken = UUID.randomUUID().toString();
            codeStorage.put(verificationToken, entity);

            response.setVerified(true);
            response.setVerificationToken(verificationToken);
        } else {
            response.setVerified(false);
        }
        return response;
    }

    /**
     * 注册个人/招聘者
     */
    @Transactional
    public AuthDTO.RegisterResponse register(AuthDTO.RegisterRequest request) {
        // 验证token
        VerificationCodeEntity verificationCode = codeStorage.get(request.getVerificationToken());
        if (verificationCode == null || !verificationCode.getEmail().equals(request.getEmail())) {
            throw new RuntimeException("验证token无效");
        }

        // 检查用户是否已存在
        LambdaQueryWrapper<UserAccountEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAccountEntity::getEmail, request.getEmail());
        UserAccountEntity existingUser = userAccountMapper.selectOne(queryWrapper);

        Long userId;
        if (existingUser != null) {
            userId =  existingUser.getUserId();
        }
        else {
            // 生成salt和密码hash
            String salt = UUID.randomUUID().toString();
            String passwordHash = passwordUtil.hashPassword(request.getPassword(), salt);

            // 创建用户账户
            UserAccountEntity user = new UserAccountEntity();
            user.setEmail(request.getEmail());
            user.setPasswordHash(passwordHash);
            user.setSalt(salt);
            user.setIsActive(true);
            user.setLoginAttempts(0);
            user.setCreatedAt(Instant.now());
            user.setUpdatedAt(Instant.now());
            userAccountMapper.insert(user);
            userId = user.getUserId();
        }

        Long roleId;

        // 根据用户类型创建对应的记录
        if ("recruiters".equalsIgnoreCase(request.getUserType())) {
            LambdaQueryWrapper<RecruiterEntity> eq = new LambdaQueryWrapper<>();
            eq.eq(RecruiterEntity::getUserId, userId);
            RecruiterEntity recruiter = recruiterMapper.selectOne(eq);
            if (recruiter != null) {
                throw new RuntimeException("用户已存在");
            }

            recruiter = new RecruiterEntity();
            recruiter.setUserId(userId);
            recruiter.setFullName(request.getName());
            recruiter.setProfessionalTitle(request.getTitle());
            recruiter.setIsVerified(false);
            recruiter.setIsActive(true);
            recruiter.setCreatedAt(Instant.now());
            recruiter.setUpdatedAt(Instant.now());
            recruiterMapper.insert(recruiter);
            roleId = recruiter.getRecruiterId();
        } else { // job_seeker
            LambdaQueryWrapper<TalentEntity> eq = new LambdaQueryWrapper<>();
            eq.eq(TalentEntity::getUserId, userId);
            TalentEntity talent = talentMapper.selectOne(eq);
            if (talent != null) {
                throw new RuntimeException("用户已存在");
            }
            talent = new TalentEntity();
            talent.setUserId(userId);
            talent.setFullName(request.getName());
            talent.setVote(0.0);
            talent.setIsActive(true);
            talent.setCreatedAt(Instant.now());
            talent.setUpdatedAt(Instant.now());
            talentMapper.insert(talent);
            roleId = talent.getId();
        }

        // 移除验证码
        codeStorage.remove(request.getVerificationToken());

        AuthDTO.RegisterResponse response = new AuthDTO.RegisterResponse();
        response.setUserId(userId);
        response.setEmail(request.getEmail());
        response.setUserType(request.getUserType());
        response.setName(request.getName());

        // 设置对应的 ID
        if ("recruiters".equalsIgnoreCase(request.getUserType())) {
            response.setRecruiterId(roleId);
        } else {
            response.setTalentId(roleId);
        }

        return response;
    }

    /**
     * 用户登录
     */
    public AuthDTO.LoginResponse login(AuthDTO.LoginRequest request) {
        LambdaQueryWrapper<UserAccountEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAccountEntity::getEmail, request.getEmail());
        UserAccountEntity user = userAccountMapper.selectOne(queryWrapper);

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (!user.getIsActive()) {
            throw new RuntimeException("账户已被禁用");
        }

        // 检查账户是否被锁定
        if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(Instant.now())) {
            throw new RuntimeException("账户已被锁定，请稍后再试");
        }

        // 验证密码
        String passwordHash = passwordUtil.hashPassword(request.getPassword(), user.getSalt());
        if (!passwordHash.equals(user.getPasswordHash())) {
            // 增加登录失败次数
            user.setLoginAttempts(user.getLoginAttempts() + 1);
            if (user.getLoginAttempts() >= 5) {
                user.setLockedUntil(Instant.now().plusSeconds(1800)); // 锁定30分钟
            }
            user.setUpdatedAt(Instant.now());
            userAccountMapper.updateById(user);
            throw new RuntimeException("密码错误");
        }

        // 重置登录失败次数
        user.setLoginAttempts(0);
        user.setLockedUntil(null);
        user.setLastLoginAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        userAccountMapper.updateById(user);

        // 查询用户类型
        String userType = "user";
        String userName = user.getEmail();

        // 检查是求职者还是招聘者
        LambdaQueryWrapper<TalentEntity> talentWrapper = new LambdaQueryWrapper<>();
        talentWrapper.eq(TalentEntity::getUserId, user.getUserId());
        TalentEntity talent = talentMapper.selectOne(talentWrapper);

        if (talent != null) {
            userType = "job_seeker";
            userName = talent.getFullName();
        } else {
            LambdaQueryWrapper<RecruiterEntity> recruiterWrapper = new LambdaQueryWrapper<>();
            recruiterWrapper.eq(RecruiterEntity::getUserId, user.getUserId());
            RecruiterEntity recruiter = recruiterMapper.selectOne(recruiterWrapper);

            if (recruiter != null) {
                userType = "recruiters";
                userName = recruiter.getFullName();
            }
        }

        String accessToken = jwtUtil.generateAccessToken(user.getUserId().toString(), user.getEmail(), userType);
        String refreshToken = jwtUtil.generateRefreshToken(user.getUserId().toString());

        AuthDTO.LoginResponse response = new AuthDTO.LoginResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setTokenType("bearer");
        response.setExpiresIn(7200);

        AuthDTO.UserInfo userInfo = new AuthDTO.UserInfo();
        userInfo.setUserId(user.getUserId());
        userInfo.setEmail(user.getEmail());
        userInfo.setUserType(userType);
        userInfo.setName(userName);
        response.setUserInfo(userInfo);

        return response;
    }

    /**
     * 刷新token
     */
    public AuthDTO.RefreshTokenResponse refreshToken(AuthDTO.RefreshTokenRequest request) {
        if (!jwtUtil.validateToken(request.getRefreshToken())) {
            throw new RuntimeException("Refresh token无效");
        }

        Claims claims = jwtUtil.getClaimsFromToken(request.getRefreshToken());
        String userId = claims.getSubject();

        String newAccessToken = jwtUtil.generateAccessToken(userId, "", "");
        String newRefreshToken = jwtUtil.generateRefreshToken(userId);

        AuthDTO.RefreshTokenResponse response = new AuthDTO.RefreshTokenResponse();
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(newRefreshToken);
        response.setTokenType("bearer");
        response.setExpiresIn(7200);

        return response;
    }
}
