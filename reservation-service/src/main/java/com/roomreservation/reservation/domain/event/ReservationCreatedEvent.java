package com.roomreservation.reservation.domain.event;

import com.roomreservation.reservation.domain.model.Reservation;

public class ReservationCreatedEvent implements ReservationEvent {
    private final Reservation reservation;

    public ReservationCreatedEvent(Reservation reservation) {
        this.reservation = reservation;
    }

    @Override
    public Reservation getReservation() {
        return reservation;
    }
} 