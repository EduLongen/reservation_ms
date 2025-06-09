package com.roomreservation.user.domain.event;

import com.roomreservation.user.domain.model.User;

public interface UserEvent {
    User getUser();
} 