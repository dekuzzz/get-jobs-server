package com.getjob.backend.dto;

import lombok.Data;
import java.util.List;

public class JobDTO {

    @Data
    public static class CreateJobRequest {
        private String title;
        private String workLocation;
        private String workType; // full_time, part_time
        private String officeMode; // online, offline
        private String minSalary;
        private String maxSalary;
        private String category;
        private String overview;
        private String responsibility;
        private String qualification;
        private String details;
    }

    @Data
    public static class CreateJobResponse {
        private Long jobId;
        private String title;
        private String status;
        private String createdAt;
    }

    @Data
    public static class JobListItem {
        private Long jobId;
        private Long companyId;
        private String companyName;
        private String category;
        private String workType;
        private String officeMode;
        private String minSalary;
        private String maxSalary;
        private Integer priority;
    }

    @Data
    public static class JobListResponse {
        private List<JobListItem> jobs;
        private Pagination pagination;
    }

    @Data
    public static class Pagination {
        private Integer page;
        private Integer size;
        private Integer total;
    }

    @Data
    public static class JobDetailResponse {
        private Long jobId;
        private String title;
        private String workLocation;
        private String workType;
        private String officeMode;
        private String minSalary;
        private String maxSalary;
        private String overview;
        private String responsibility;
        private String qualification;
        private String details;
        private String createdAt;
    }
}

