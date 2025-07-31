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
@Table(name = "talents")
@NoArgsConstructor
public class TalentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "talent_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clerk_user_id", nullable = false)
    private ClerkUserEntity clerkUserEntity;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "vote")
    private Double vote;

    @OneToMany(mappedBy = "talentEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TalentLinkEntity> links = new ArrayList<>();

    @OneToOne(mappedBy = "talentEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private ResumeEntity resumeEntity = new ResumeEntity();

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();
}
