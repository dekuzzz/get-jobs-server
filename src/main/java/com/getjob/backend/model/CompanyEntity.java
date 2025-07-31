package com.getjob.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Table(name = "companies")
@NoArgsConstructor
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clerk_user_id", nullable = false)
    private ClerkUserEntity clerkUserEntity;

    @Column(name = "full_name", length = 255)
    private String fullName;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "official_website")
    private String officialWebsite;

    @Column(length = 255)
    private String location;

    @Column(name = "language_code", length = 2)
    private String languageCode;

    @Column(length = 50)
    private String size;

    @Column(name = "category_tags")
    private String categoryTags;

    @Column(name = "email_address", length = 255)
    private String emailAddress;

    @Column(name = "vote")
    private Double vote;

    @OneToMany(mappedBy = "companyEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompanyContentEntity> contents = new ArrayList<>();

    @OneToMany(mappedBy = "companyEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompanyLinkEntity> links = new ArrayList<>();

    @OneToMany(mappedBy = "companyEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobEntity> jobEntities = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();
}
