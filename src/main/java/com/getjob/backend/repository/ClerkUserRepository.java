package com.getjob.backend.repository;

import com.getjob.backend.model.ClerkUserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClerkUserRepository extends JpaRepository<ClerkUserEntity, String> {
}
