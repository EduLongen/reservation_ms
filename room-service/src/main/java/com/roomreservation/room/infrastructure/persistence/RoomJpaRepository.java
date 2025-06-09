package com.roomreservation.room.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomJpaRepository extends JpaRepository<RoomJpaEntity, Long> {
    List<RoomJpaEntity> findByActiveTrue();
} 