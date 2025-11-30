package com.getjob.backend.dto;

import lombok.Data;
import java.util.List;

public class RoleDTO {

    @Data
    public static class Member {
        private Long recruiterId;
        private String name;
        private String title;
    }

    @Data
    public static class SearchRolesResponse {
        private List<Member> members;
    }

    @Data
    public static class SearchRolesRequest {
        private String fullName;
        private String emailAddress;
    }

    @Data
    public static class AddRoleRequest {
        private List<MemberRequest> members;
    }

    @Data
    public static class MemberRequest {
        private Long userId;
        private Long recruiterId;
    }
}

