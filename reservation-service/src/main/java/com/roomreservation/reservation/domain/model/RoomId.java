package com.roomreservation.reservation.domain.model;

import lombok.Value;

@Value
public class RoomId {
    Long value;

    public RoomId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Room ID must be a positive value");
        }
        this.value = value;
    }
} 