package com.getjob.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.getjob.backend.dto.RoleDTO;
import com.getjob.backend.mapper.*;
import com.getjob.backend.model.*;
import com.getjob.backend.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RecruiterMapper recruiterMapper;
    private final CompanyRecruiterMapper companyRecruiterMapper;
    private final CompanyMapper companyMapper;
    private final UserAccountMapper userAccountMapper;

    @Override
    public RoleDTO.SearchRolesResponse searchRoles(String userId) {
        Long userIdLong = Long.parseLong(userId);
        
        // 验证是否为招聘者
        RecruiterEntity currentRecruiter = recruiterMapper.selectOne(
                new LambdaQueryWrapper<RecruiterEntity>()
                        .eq(RecruiterEntity::getUserId, userIdLong)
        );

        if (currentRecruiter == null) {
            throw new RuntimeException("无权访问");
        }

        // 搜索所有招聘者
        List<RecruiterEntity> recruiters = recruiterMapper.selectList(
                new LambdaQueryWrapper<RecruiterEntity>()
                        .eq(RecruiterEntity::getIsActive, true)
        );

        List<RoleDTO.Member> members = recruiters.stream().map(recruiter -> {
            RoleDTO.Member member = new RoleDTO.Member();
            member.setRecruiterId(recruiter.getRecruiterId());
            member.setName(recruiter.getFullName());
            member.setTitle(recruiter.getProfessionalTitle());
            return member;
        }).collect(Collectors.toList());

        RoleDTO.SearchRolesResponse response = new RoleDTO.SearchRolesResponse();
        response.setMembers(members);
        return response;
    }

    /**
     * 根据全名或邮箱搜索招聘者
     */
    public RoleDTO.SearchRolesResponse searchRolesByNameOrEmail(String userId, String fullName, String emailAddress) {
        Long userIdLong = Long.parseLong(userId);
        
        // 验证是否为招聘者
        RecruiterEntity currentRecruiter = recruiterMapper.selectOne(
                new LambdaQueryWrapper<RecruiterEntity>()
                        .eq(RecruiterEntity::getUserId, userIdLong)
        );

        if (currentRecruiter == null) {
            throw new RuntimeException("无权访问");
        }

        LambdaQueryWrapper<RecruiterEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RecruiterEntity::getIsActive, true);
        
        if (fullName != null && !fullName.isEmpty()) {
            queryWrapper.like(RecruiterEntity::getFullName, fullName);
        }
        
        if (emailAddress != null && !emailAddress.isEmpty()) {
            // 需要通过 userId 关联 user_accounts 表查询邮箱
            List<UserAccountEntity> users = userAccountMapper.selectList(
                    new LambdaQueryWrapper<UserAccountEntity>()
                            .like(UserAccountEntity::getEmail, emailAddress)
            );
            
            if (users.isEmpty()) {
                RoleDTO.SearchRolesResponse response = new RoleDTO.SearchRolesResponse();
                response.setMembers(new ArrayList<>());
                return response;
            }
            
            List<Long> userIds = users.stream().map(UserAccountEntity::getUserId).collect(Collectors.toList());
            queryWrapper.in(RecruiterEntity::getUserId, userIds);
        }

        List<RecruiterEntity> recruiters = recruiterMapper.selectList(queryWrapper);

        List<RoleDTO.Member> members = recruiters.stream().map(recruiter -> {
            RoleDTO.Member member = new RoleDTO.Member();
            member.setRecruiterId(recruiter.getRecruiterId());
            member.setName(recruiter.getFullName());
            member.setTitle(recruiter.getProfessionalTitle());
            return member;
        }).collect(Collectors.toList());

        RoleDTO.SearchRolesResponse response = new RoleDTO.SearchRolesResponse();
        response.setMembers(members);
        return response;
    }

    /**
     * 搜索已有授权（已加入公司的招聘者）
     */
    public RoleDTO.SearchRolesResponse searchExistingRoles(String userId) {
        Long userIdLong = Long.parseLong(userId);
        
        // 验证是否为招聘者并获取其公司
        RecruiterEntity currentRecruiter = recruiterMapper.selectOne(
                new LambdaQueryWrapper<RecruiterEntity>()
                        .eq(RecruiterEntity::getUserId, userIdLong)
        );

        if (currentRecruiter == null) {
            throw new RuntimeException("无权访问");
        }

        // 获取招聘者所属的公司
        CompanyRecruiterEntity companyRecruiter = companyRecruiterMapper.selectOne(
                new LambdaQueryWrapper<CompanyRecruiterEntity>()
                        .eq(CompanyRecruiterEntity::getRecruiterId, currentRecruiter.getRecruiterId())
                        .eq(CompanyRecruiterEntity::getStatus, "active")
        );

        if (companyRecruiter == null) {
            throw new RuntimeException("您还未加入任何公司");
        }

        // 查询该公司的所有招聘者
        List<CompanyRecruiterEntity> companyRecruiters = companyRecruiterMapper.selectList(
                new LambdaQueryWrapper<CompanyRecruiterEntity>()
                        .eq(CompanyRecruiterEntity::getCompanyId, companyRecruiter.getCompanyId())
                        .eq(CompanyRecruiterEntity::getStatus, "active")
        );

        List<RoleDTO.Member> members = companyRecruiters.stream().map(cr -> {
            RecruiterEntity recruiter = recruiterMapper.selectById(cr.getRecruiterId());
            if (recruiter != null) {
                RoleDTO.Member member = new RoleDTO.Member();
                member.setRecruiterId(recruiter.getRecruiterId());
                member.setName(recruiter.getFullName());
                member.setTitle(recruiter.getProfessionalTitle());
                return member;
            }
            return null;
        }).filter(m -> m != null).collect(Collectors.toList());

        RoleDTO.SearchRolesResponse response = new RoleDTO.SearchRolesResponse();
        response.setMembers(members);
        return response;
    }

    @Override
    @Transactional
    public void addRoles(String userId, RoleDTO.AddRoleRequest request) {
        Long userIdLong = Long.parseLong(userId);
        
        // 验证是否为招聘者
        RecruiterEntity currentRecruiter = recruiterMapper.selectOne(
                new LambdaQueryWrapper<RecruiterEntity>()
                        .eq(RecruiterEntity::getUserId, userIdLong)
        );

        if (currentRecruiter == null) {
            throw new RuntimeException("无权操作");
        }

        // 获取招聘者所属的公司，并验证是否为 admin
        CompanyRecruiterEntity currentCompanyRecruiter = companyRecruiterMapper.selectOne(
                new LambdaQueryWrapper<CompanyRecruiterEntity>()
                        .eq(CompanyRecruiterEntity::getRecruiterId, currentRecruiter.getRecruiterId())
                        .eq(CompanyRecruiterEntity::getStatus, "active")
        );

        if (currentCompanyRecruiter == null) {
            throw new RuntimeException("您还未加入任何公司");
        }

        // 验证权限（必须是 owner 或 admin）
        if (!"owner".equals(currentCompanyRecruiter.getRole()) && !"admin".equals(currentCompanyRecruiter.getRole())) {
            throw new RuntimeException("无权操作，仅公司管理员可以添加成员");
        }

        // 添加成员
        for (RoleDTO.MemberRequest member : request.getMembers()) {
            RecruiterEntity recruiter = recruiterMapper.selectById(member.getRecruiterId());
            if (recruiter == null) {
                throw new RuntimeException("招聘者不存在: " + member.getRecruiterId());
            }

            // 检查是否已经加入
            CompanyRecruiterEntity existing = companyRecruiterMapper.selectOne(
                    new LambdaQueryWrapper<CompanyRecruiterEntity>()
                            .eq(CompanyRecruiterEntity::getCompanyId, currentCompanyRecruiter.getCompanyId())
                            .eq(CompanyRecruiterEntity::getRecruiterId, member.getRecruiterId())
            );

            if (existing != null) {
                // 如果已存在但状态不是active，更新状态
                if (!"active".equals(existing.getStatus())) {
                    existing.setStatus("pending");
                    existing.setUpdatedAt(Instant.now());
                    companyRecruiterMapper.updateById(existing);
                }
                continue;
            }

            // 创建新的公司招聘者关联
            CompanyRecruiterEntity newCompanyRecruiter = new CompanyRecruiterEntity();
            newCompanyRecruiter.setCompanyId(currentCompanyRecruiter.getCompanyId());
            newCompanyRecruiter.setRecruiterId(member.getRecruiterId());
            newCompanyRecruiter.setRole("recruiter");
            newCompanyRecruiter.setStatus("pending");
            newCompanyRecruiter.setInvitedBy(currentRecruiter.getRecruiterId());
            newCompanyRecruiter.setCreatedAt(Instant.now());
            newCompanyRecruiter.setUpdatedAt(Instant.now());
            companyRecruiterMapper.insert(newCompanyRecruiter);
        }
    }

    @Override
    @Transactional
    public void deleteRole(String userId, Long recruiterId) {
        Long userIdLong = Long.parseLong(userId);
        
        // 验证是否为招聘者
        RecruiterEntity currentRecruiter = recruiterMapper.selectOne(
                new LambdaQueryWrapper<RecruiterEntity>()
                        .eq(RecruiterEntity::getUserId, userIdLong)
        );

        if (currentRecruiter == null) {
            throw new RuntimeException("无权操作");
        }

        // 获取招聘者所属的公司，并验证是否为 admin
        CompanyRecruiterEntity currentCompanyRecruiter = companyRecruiterMapper.selectOne(
                new LambdaQueryWrapper<CompanyRecruiterEntity>()
                        .eq(CompanyRecruiterEntity::getRecruiterId, currentRecruiter.getRecruiterId())
                        .eq(CompanyRecruiterEntity::getStatus, "active")
        );

        if (currentCompanyRecruiter == null) {
            throw new RuntimeException("您还未加入任何公司");
        }

        // 验证权限（必须是 owner 或 admin）
        if (!"owner".equals(currentCompanyRecruiter.getRole()) && !"admin".equals(currentCompanyRecruiter.getRole())) {
            throw new RuntimeException("无权操作，仅公司管理员可以删除成员");
        }

        // 查找要删除的成员
        CompanyRecruiterEntity targetCompanyRecruiter = companyRecruiterMapper.selectOne(
                new LambdaQueryWrapper<CompanyRecruiterEntity>()
                        .eq(CompanyRecruiterEntity::getCompanyId, currentCompanyRecruiter.getCompanyId())
                        .eq(CompanyRecruiterEntity::getRecruiterId, recruiterId)
        );

        if (targetCompanyRecruiter == null) {
            throw new RuntimeException("该成员不在公司中");
        }

        // 不能删除 owner
        if ("owner".equals(targetCompanyRecruiter.getRole())) {
            throw new RuntimeException("不能删除公司所有者");
        }

        // 更改状态为 suspended
        targetCompanyRecruiter.setStatus("suspended");
        targetCompanyRecruiter.setUpdatedAt(Instant.now());
        companyRecruiterMapper.updateById(targetCompanyRecruiter);
    }
}
