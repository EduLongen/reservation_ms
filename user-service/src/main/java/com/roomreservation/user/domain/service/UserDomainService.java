package com.roomreservation.user.domain.service;

import com.roomreservation.user.domain.event.UserEventPublisher;
import com.roomreservation.user.domain.event.UserUpdatedEvent;
import com.roomreservation.user.domain.event.UserCreatedEvent;
import com.roomreservation.user.domain.event.UserDeletedEvent;
import com.roomreservation.user.domain.model.User;
import com.roomreservation.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserDomainService {

    private final UserRepository userRepository;
    private final UserEventPublisher eventPublisher;

    public UserDomainService(UserRepository userRepository, UserEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUser(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        User savedUser = userRepository.save(user);
        eventPublisher.publish(new UserCreatedEvent(savedUser));
        return savedUser;
    }

    public void updateUser(User user) {
        User updatedUser = userRepository.save(user);
        eventPublisher.publish(new UserUpdatedEvent(updatedUser));
    }

    public void deleteUser(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            userRepository.deleteById(id);
            eventPublisher.publish(new UserDeletedEvent(user));
        });
    }

    public List<User> listActiveUsers() {
        return userRepository.findByActiveTrue();
    }

    public Optional<User> findUserByRegistrationNumber(String registrationNumber) {
        return userRepository.findByRegistrationNumber(registrationNumber);
    }
} 