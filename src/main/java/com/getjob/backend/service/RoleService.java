package com.getjob.backend.service;

import com.getjob.backend.dto.RoleDTO;

public interface RoleService {
    RoleDTO.SearchRolesResponse searchRoles(String userId);
    void addRoles(String userId, RoleDTO.AddRoleRequest request);
    void deleteRole(String userId, Long recruiterId);
}

