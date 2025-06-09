package com.roomreservation.reservation.domain.event;

import com.roomreservation.reservation.domain.model.Reservation;

public interface ReservationEvent {
    Reservation getReservation();
} 