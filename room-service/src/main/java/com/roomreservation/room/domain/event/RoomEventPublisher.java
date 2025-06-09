package com.roomreservation.room.domain.event;

public interface RoomEventPublisher {
    void publish(RoomEvent event);
} 