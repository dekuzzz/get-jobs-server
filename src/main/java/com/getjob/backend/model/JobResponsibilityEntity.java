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
@Table(name = "job_responsibility")
@NoArgsConstructor
public class JobResponsibilityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_responsibility_id")
    private Long jobResponsibilityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private JobEntity jobEntity;

    @Column(name = "language_code", length = 2, nullable = false)
    private String languageCode;

    @Column(name = "responsibility_text", nullable = false)
    private String responsibilityText;

}
