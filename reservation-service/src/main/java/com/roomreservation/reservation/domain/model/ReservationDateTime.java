package com.roomreservation.reservation.domain.model;

import lombok.Value;
import java.time.LocalDateTime;

@Value
public class ReservationDateTime {
    LocalDateTime value;

    public ReservationDateTime(LocalDateTime value) {
        if (value == null) {
            throw new IllegalArgumentException("Reservation date and time cannot be null");
        }
        if (value.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Reservation date and time cannot be in the past");
        }
        this.value = value;
    }

    public boolean conflictsWith(ReservationDateTime other) {
        return this.value.isEqual(other.value) || 
               (this.value.isBefore(other.value.plusHours(1)) && 
                this.value.plusHours(1).isAfter(other.value));
    }
} 