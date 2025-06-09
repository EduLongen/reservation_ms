package com.roomreservation.user.domain.event;

import com.roomreservation.user.domain.model.User;

public class UserUpdatedEvent implements UserEvent {
    private final User user;

    public UserUpdatedEvent(User user) {
        this.user = user;
    }

    @Override
    public User getUser() {
        return user;
    }
} 