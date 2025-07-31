package com.getjob.backend.repository;
import com.getjob.backend.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TalentRepository extends JpaRepository<TalentEntity, Long>, JpaSpecificationExecutor<TalentEntity> {
}