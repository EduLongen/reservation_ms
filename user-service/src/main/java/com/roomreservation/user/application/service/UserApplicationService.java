package com.roomreservation.user.application.service;

import com.roomreservation.user.domain.model.User;
import com.roomreservation.user.domain.service.UserDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserApplicationService {

    private final UserDomainService userDomainService;

    public UserApplicationService(UserDomainService userDomainService) {
        this.userDomainService = userDomainService;
    }

    public List<User> listUsers() {
        return userDomainService.listUsers();
    }

    public Optional<User> findUser(Long id) {
        return userDomainService.findUser(id);
    }

    public User createUser(User user) {
        return userDomainService.createUser(user);
    }

    public void updateUser(User user) {
        userDomainService.updateUser(user);
    }

    public void deleteUser(Long id) {
        userDomainService.deleteUser(id);
    }

    public List<User> listActiveUsers() {
        return userDomainService.listActiveUsers();
    }

    public Optional<User> findUserByRegistrationNumber(String registrationNumber) {
        return userDomainService.findUserByRegistrationNumber(registrationNumber);
    }
} 