package com.getjob.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Table(name = "resume_skills")
@NoArgsConstructor
public class ResumeSkillEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_skill_id")
    private Long resumeSkillId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private ResumeEntity resumeEntity;

    @Column(name = "language_code", length = 2, nullable = false)
    private String languageCode;

    @Column(name = "skill_name", length = 100, nullable = false)
    private String skillName;

    @Column(name = "proficiency", length = 50)
    private String proficiency;

    // Getters and Setters
}