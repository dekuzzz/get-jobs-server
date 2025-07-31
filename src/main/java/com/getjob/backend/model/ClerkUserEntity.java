package com.getjob.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Table(name = "clerk_users")
@NoArgsConstructor
public class ClerkUserEntity {
    @Id
    @Column(name = "clerk_user_id", length = 255)
    private String clerkUserId;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    @OneToOne(mappedBy = "clerkUserEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private TalentEntity talentEntity = new TalentEntity();

    @OneToOne(mappedBy = "clerkUserEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private CompanyEntity companies = new CompanyEntity();
}