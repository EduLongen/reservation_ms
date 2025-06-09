package com.roomreservation.reservation.domain.repository;

import com.roomreservation.reservation.domain.model.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    List<Reservation> findAll();
    List<Reservation> findAll(Sort sort);
    Page<Reservation> findAll(Pageable pageable);
    List<Reservation> findAllById(Iterable<Long> longs);
    long count();
    void deleteById(Long aLong);
    void delete(Reservation entity);
    void deleteAllById(Iterable<? extends Long> longs);
    void deleteAll(Iterable<? extends Reservation> entities);
    void deleteAll();
    <S extends Reservation> S save(S entity);
    <S extends Reservation> List<S> saveAll(Iterable<S> entities);
    Optional<Reservation> findById(Long aLong);
    boolean existsById(Long aLong);
    void flush();
    <S extends Reservation> S saveAndFlush(S entity);
    <S extends Reservation> List<S> saveAllAndFlush(Iterable<S> entities);
    void deleteAllInBatch(Iterable<Reservation> entities);
    void deleteAllByIdInBatch(Iterable<Long> longs);
    void deleteAllInBatch();
    Reservation getOne(Long aLong);
    Reservation getById(Long aLong);
    Reservation getReferenceById(Long aLong);
    List<Reservation> findByRoomIdAndDateTimeBetween(Long roomId, LocalDateTime start, LocalDateTime end);
    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByRoomId(Long roomId);
} 