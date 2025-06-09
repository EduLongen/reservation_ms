package com.roomreservation.user.domain.repository;

import com.roomreservation.user.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository {
    List<User> findAll();
    List<User> findAll(Sort sort);
    Page<User> findAll(Pageable pageable);
    List<User> findAllById(Iterable<Long> longs);
    long count();
    void deleteById(Long aLong);
    void delete(User entity);
    void deleteAllById(Iterable<? extends Long> longs);
    void deleteAll(Iterable<? extends User> entities);
    void deleteAll();
    <S extends User> S save(S entity);
    <S extends User> List<S> saveAll(Iterable<S> entities);
    Optional<User> findById(Long aLong);
    boolean existsById(Long aLong);
    void flush();
    <S extends User> S saveAndFlush(S entity);
    <S extends User> List<S> saveAllAndFlush(Iterable<S> entities);
    void deleteAllInBatch(Iterable<User> entities);
    void deleteAllByIdInBatch(Iterable<Long> longs);
    void deleteAllInBatch();
    User getOne(Long aLong);
    User getById(Long aLong);
    User getReferenceById(Long aLong);
    List<User> findByActiveTrue();
    Optional<User> findByRegistrationNumber(String registrationNumber);
} 