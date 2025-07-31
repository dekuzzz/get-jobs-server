package com.getjob.backend.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
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
@Table(name = "jobs")
@NoArgsConstructor
public class JobEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long jobId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyEntity companyEntity;

    @Column(name = "language_code", length = 2, nullable = false)
    private String languageCode;

    @Column(name = "job_name", length = 255)
    private String jobName;

    @Column(name = "office_mode", length = 100)
    private String officeMode;

    @Column(name = "work_nature", length = 20, nullable = false)
    private String workNature;

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "min_salary")
    private BigDecimal minSalary;

    @Column(name = "max_salary")
    private BigDecimal maxSalary;

    @Column(name = "currency_code", length = 3)
    private String currencyCode;

    @Column(name = "settlement", length = 100)
    private String settlement;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    @Column(name = "update_time", nullable = false)
    private Instant updateTime = Instant.now();

    @OneToMany(mappedBy = "jobEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobResponsibilityEntity> responsibilities = new ArrayList<>();

    @OneToMany(mappedBy = "jobEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobQualificationEntity> qualifications = new ArrayList<>();

    @OneToMany(mappedBy = "jobEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobContentEntity> contents = new ArrayList<>();
}
