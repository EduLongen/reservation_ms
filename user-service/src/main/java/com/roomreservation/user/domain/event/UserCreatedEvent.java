package com.roomreservation.user.domain.event;

import com.roomreservation.user.domain.model.User;

public class UserCreatedEvent implements UserEvent {
    private final User user;

    public UserCreatedEvent(User user) {
        this.user = user;
    }

    @Override
    public User getUser() {
        return user;
    }
} 