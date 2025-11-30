package com.getjob.backend.dto;

import lombok.Data;
import java.util.List;

public class CompanyDTO {

    @Data
    public static class CompanyInfoResponse {
        private String companyName;
        private String avatar;
        private String slogan;
        private String companyWebsite;
        private Integer staffsNumber;
        private String location;
        private String email;
        private String phone;
        private String twitterLink;
        private String qqLink;
        private String githubLink;
        private String introduction;
        private List<String> projectCategory;
        private List<String> supportedChains;
    }

    @Data
    public static class UpdateCompanyRequest {
        private String location;
        private String email;
        private String phone;
        private String twitterLink;
        private String qqLink;
        private String githubLink;
        private String introduction;
    }

    @Data
    public static class CreateCompanyRequest {
        private String companyName;
        private String avatar;
        private String slogan;
        private String companyWebsite;
        private Integer staffsNumber;
        private String location;
        private String email;
        private String phone;
        private String twitterLink;
        private String telegramLink;
        private String wechatLink;
        private String githubLink;
        private String introduction;
        private List<String> projectCategory;
        private List<String> supportedChains;
        private List<String> skillsTag;
    }
}

