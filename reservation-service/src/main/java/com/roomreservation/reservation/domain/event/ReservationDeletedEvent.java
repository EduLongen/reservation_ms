package com.roomreservation.reservation.domain.event;

import com.roomreservation.reservation.domain.model.Reservation;

public class ReservationDeletedEvent implements ReservationEvent {
    private final Reservation reservation;

    public ReservationDeletedEvent(Reservation reservation) {
        this.reservation = reservation;
    }

    @Override
    public Reservation getReservation() {
        return reservation;
    }
} 