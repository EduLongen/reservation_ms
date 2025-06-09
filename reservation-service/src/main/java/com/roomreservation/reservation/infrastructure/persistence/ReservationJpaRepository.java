package com.roomreservation.reservation.infrastructure.persistence;

import com.roomreservation.reservation.infrastructure.persistence.entity.ReservationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationJpaRepository extends JpaRepository<ReservationJpaEntity, Long> {
    List<ReservationJpaEntity> findByRoomIdAndDateTimeBetween(Long roomId, LocalDateTime start, LocalDateTime end);
    List<ReservationJpaEntity> findByUserId(Long userId);
    List<ReservationJpaEntity> findByRoomId(Long roomId);
} 