package com.roomreservation.reservation.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    private Long id;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Long roomId;
    private Long userId;

    public Reservation(Long id, Long roomId, Long userId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.id = id;
        this.roomId = roomId;
        this.userId = userId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
} 