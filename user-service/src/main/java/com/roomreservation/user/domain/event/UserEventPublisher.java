package com.roomreservation.user.domain.event;

public interface UserEventPublisher {
    void publish(UserEvent event);
} 