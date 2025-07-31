package com.getjob.backend.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * 人才列表响应DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "人才列表响应")
public class TalentListResponse {
    /**
     * 请求是否成功
     */
    @Schema(description = "请求是否成功", example = "true")
    private boolean success;

    /**
     * 响应数据
     */
    @Schema(description = "响应数据")
    private Data data;

    /**
     * 响应数据内容
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "响应数据内容")
    public static class Data {
        /**
         * 人才列表
         */
        @Schema(description = "人才列表")
        private List<Talent> talents;

        /**
         * 分页信息
         */
        @Schema(description = "分页信息")
        private Pagination pagination;
    }

    /**
     * 分页信息
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "分页信息")
    public static class Pagination {
        /**
         * 当前页码
         */
        @Schema(description = "当前页码", example = "1")
        private int currentPage;

        /**
         * 总页数
         */
        @Schema(description = "总页数", example = "5")
        private int totalPages;

        /**
         * 总记录数
         */
        @Schema(description = "总记录数", example = "42")
        private int totalCount;

        /**
         * 每页数量
         */
        @Schema(description = "每页数量", example = "8")
        private int pageSize;
    }

    /**
     * 人才信息DTO
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "人才信息")
    public static class Talent {
        /**
         * 人才ID
         */
        @Schema(description = "人才ID", example = "123")
        private Long id;

        /**
         * 招聘者信息
         */
        @Schema(description = "招聘者信息")
        private Recruiter recruiter;

        /**
         * 职位标题
         */
        @Schema(description = "职位标题", example = "高级Java开发工程师")
        private String title;

        /**
         * 标签列表
         */
        @Schema(description = "标签列表", example = "[\"Full-time\", \"On-site\"]")
        private List<String> tags;

        /**
         * 技能要求列表
         */
        @Schema(description = "技能要求列表", example = "[\"3+ YOE\", \"Java\", \"Spring Boot\"]")
        private List<String> skills;

        /**
         * 薪资范围
         */
        @Schema(description = "薪资范围", example = "$25,000-40,000 per month")
        private String salary;

        /**
         * 发布时间
         */
        @Schema(description = "发布时间", example = "23 hours ago posted")
        private String posted;

        /**
         * 是否推荐
         */
        @Schema(description = "是否推荐", example = "true")
        private boolean featured;
    }

    /**
     * 招聘者信息DTO
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "招聘者信息")
    public static class Recruiter {
        /**
         * 招聘者姓名
         */
        @Schema(description = "招聘者姓名", example = "张三")
        private String name;

        /**
         * 招聘者头像URL
         */
        @Schema(description = "招聘者头像URL", example = "https://example.com/avatar.jpg")
        private String avatar;
    }
}
