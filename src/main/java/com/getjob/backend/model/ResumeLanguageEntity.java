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
@Table(name = "resume_languages")
@NoArgsConstructor
public class ResumeLanguageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_language_id")
    private Long resumeLanguageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private ResumeEntity resumeEntity;

    @Column(name = "language_code", length = 2, nullable = false)
    private String languageCode;

    @Column(name = "language_name", length = 100, nullable = false)
    private String languageName;

    @Column(name = "proficiency", length = 50)
    private String proficiency;

    // Getters and Setters
}