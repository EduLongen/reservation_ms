package com.roomreservation.reservation.infrastructure.persistence;

import com.roomreservation.reservation.domain.model.Reservation;
import com.roomreservation.reservation.domain.repository.ReservationRepository;
import com.roomreservation.reservation.infrastructure.persistence.entity.ReservationJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
@Transactional
public class ReservationRepositoryImpl implements ReservationRepository {

    private final ReservationJpaRepository jpaRepository;

    public ReservationRepositoryImpl(ReservationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Reservation> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findAll(Sort sort) {
        return jpaRepository.findAll(sort).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Reservation> findAll(Pageable pageable) {
        var page = jpaRepository.findAll(pageable);
        return new PageImpl<>(
                page.getContent().stream()
                        .map(this::toDomain)
                        .collect(Collectors.toList()),
                pageable,
                page.getTotalElements()
        );
    }

    @Override
    public List<Reservation> findAllById(Iterable<Long> longs) {
        return jpaRepository.findAllById(longs).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }

    @Override
    public void deleteById(Long aLong) {
        jpaRepository.deleteById(aLong);
    }

    @Override
    public void delete(Reservation entity) {
        jpaRepository.delete(toEntity(entity));
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        jpaRepository.deleteAllById(longs);
    }

    @Override
    public void deleteAll(Iterable<? extends Reservation> entities) {
        jpaRepository.deleteAll(
                StreamSupport.stream(entities.spliterator(), false)
                        .map(this::toEntity)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }

    @Override
    public <S extends Reservation> S save(S entity) {
        var savedEntity = jpaRepository.save(toEntity(entity));
        return (S) toDomain(savedEntity);
    }

    @Override
    public <S extends Reservation> List<S> saveAll(Iterable<S> entities) {
        var savedEntities = jpaRepository.saveAll(
                StreamSupport.stream(entities.spliterator(), false)
                        .map(this::toEntity)
                        .collect(Collectors.toList())
        );
        return savedEntities.stream()
                .map(this::toDomain)
                .map(entity -> (S) entity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Reservation> findById(Long aLong) {
        return jpaRepository.findById(aLong)
                .map(this::toDomain);
    }

    @Override
    public boolean existsById(Long aLong) {
        return jpaRepository.existsById(aLong);
    }

    @Override
    public void flush() {
        jpaRepository.flush();
    }

    @Override
    public <S extends Reservation> S saveAndFlush(S entity) {
        var savedEntity = jpaRepository.saveAndFlush(toEntity(entity));
        return (S) toDomain(savedEntity);
    }

    @Override
    public <S extends Reservation> List<S> saveAllAndFlush(Iterable<S> entities) {
        var savedEntities = jpaRepository.saveAllAndFlush(
                StreamSupport.stream(entities.spliterator(), false)
                        .map(this::toEntity)
                        .collect(Collectors.toList())
        );
        return savedEntities.stream()
                .map(this::toDomain)
                .map(entity -> (S) entity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllInBatch(Iterable<Reservation> entities) {
        jpaRepository.deleteAllInBatch(
                StreamSupport.stream(entities.spliterator(), false)
                        .map(this::toEntity)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {
        jpaRepository.deleteAllByIdInBatch(longs);
    }

    @Override
    public void deleteAllInBatch() {
        jpaRepository.deleteAllInBatch();
    }

    @Override
    public Reservation getOne(Long aLong) {
        return toDomain(jpaRepository.getOne(aLong));
    }

    @Override
    public Reservation getById(Long aLong) {
        return toDomain(jpaRepository.getById(aLong));
    }

    @Override
    public Reservation getReferenceById(Long aLong) {
        return toDomain(jpaRepository.getReferenceById(aLong));
    }

    @Override
    public List<Reservation> findByRoomIdAndDateTimeBetween(Long roomId, LocalDateTime start, LocalDateTime end) {
        return jpaRepository.findByRoomIdAndStartDateTimeBetween(roomId, start, end).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findByRoomId(Long roomId) {
        return jpaRepository.findByRoomId(roomId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private ReservationJpaEntity toEntity(Reservation reservation) {
        var entity = new ReservationJpaEntity();
        entity.setId(reservation.getId());
        entity.setRoomId(reservation.getRoomId());
        entity.setUserId(reservation.getUserId());
        entity.setStartDateTime(reservation.getStartDateTime());
        entity.setEndDateTime(reservation.getEndDateTime());
        return entity;
    }

    private Reservation toDomain(ReservationJpaEntity entity) {
        return new Reservation(
                entity.getId(),
                entity.getStartDateTime(),
                entity.getEndDateTime(),
                entity.getRoomId(),
                entity.getUserId()
        );
    }
} 