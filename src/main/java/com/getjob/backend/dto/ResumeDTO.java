package com.getjob.backend.dto;

import lombok.Data;
import java.util.List;

public class ResumeDTO {

    @Data
    public static class PersonDetail {
        private String name;
        private String phoneNumber;
        private String emailAddress;
        private String telegram;
        private String wechat;
    }

    @Data
    public static class CreateResumeRequest {
        private String title;
        private List<PersonDetail> personDetail;
        private String education;
        private String language;
        private Integer web3; // 以年为单位
        private Integer visible; // 0 or 1
        private String expectedLocation;
        private String workType; // full_time, part_time
        private String officeMode; // online, offline
        private String minSalary;
        private String maxSalary;
        private List<String> skills;
        private String experience;
        private String advantage;
    }

    @Data
    public static class CreateResumeResponse {
        private Long resumeId;
        private String updatedAt;
    }

    @Data
    public static class ResumeDetailResponse {
        private Long resumeId;
        private String title;
        private List<PersonDetail> personDetail;
        private String education;
        private String language;
        private Integer web3;
        private Integer visible;
        private String expectedLocation;
        private String workType;
        private String officeMode;
        private String minSalary;
        private String maxSalary;
        private List<String> skills;
        private String experience;
        private String advantage;
        private String createdAt;
        private String updatedAt;
    }
}

