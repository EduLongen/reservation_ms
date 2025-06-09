package com.roomreservation.user.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {
    List<UserJpaEntity> findByActiveTrue();
    Optional<UserJpaEntity> findByRegistrationNumber(String registrationNumber);
} 