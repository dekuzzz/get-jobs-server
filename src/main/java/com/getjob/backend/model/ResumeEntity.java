package com.getjob.backend.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Table(name = "resumes")
@NoArgsConstructor
public class ResumeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_id")
    private Long resumeId;

    @OneToOne
    @JoinColumn(name = "talent_id", nullable = false)
    private TalentEntity talentEntity;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "email_address", length = 255)
    private String emailAddress;

    @Column(name = "telegram", length = 100)
    private String telegram;

    @Column(name = "wechat", length = 100)
    private String wechat;

    @Column(name = "office_mode", length = 100)
    private String officeMode;

    @Column(name = "work_nature", length = 20, nullable = false)
    private String workNature;

    @Column(name = "permission", length = 100)
    private String permission;

    @Column(name = "position", length = 100)
    private String position;

    @Column(name = "min_salary")
    private Double minSalary;

    @Column(name = "max_salary")
    private Double maxSalary;

    @Column(name = "currency_code", length = 3)
    private String currencyCode;

    @Column(name = "settlement", length = 100)
    private String settlement;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    @OneToMany(mappedBy = "resumeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumeAdvantageEntity> advantages = new ArrayList<>();

    @OneToMany(mappedBy = "resumeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumeEducationEntity> educations = new ArrayList<>();

    @OneToMany(mappedBy = "resumeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumeExperienceEntity> experiences = new ArrayList<>();

    @OneToMany(mappedBy = "resumeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumeSkillEntity> skills = new ArrayList<>();

    @OneToMany(mappedBy = "resumeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumeLanguageEntity> languages = new ArrayList<>();
}
