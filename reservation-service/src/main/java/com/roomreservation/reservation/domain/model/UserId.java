package com.roomreservation.reservation.domain.model;

import lombok.Value;

@Value
public class UserId {
    Long value;

    public UserId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("User ID must be a positive value");
        }
        this.value = value;
    }
} 